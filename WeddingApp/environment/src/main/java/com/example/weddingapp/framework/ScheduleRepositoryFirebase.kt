package com.example.weddingapp.framework

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.domain.interactors.ErrorType
import com.example.domain.interactors.ScheduleInteractor
import com.example.domain.models.EventModel
import com.example.domain.utils.Outcome
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

class ScheduleRepositoryFirebase(private val context: Context) : ScheduleInteractor {

    private companion object {
        const val EVENTS_COLLECTION_STRING = "events"
        const val MAIN_EVENT_COLLECTION_STRING = "main"
        const val MAIN_EVENT_DOCUMENT_ID = "main-event"
        const val MESSAGE_ERROR_CONNECTIVITY = "Connection error from schedule repository."
        const val MESSAGE_ERROR_GENERIC = "Generic error in schedule repository."
        const val MESSAGE_ERROR_NOT_FOUND = "Document or list not found in repository."
    }

    private val firebaseDb = Firebase.firestore
    private val eventsDb = firebaseDb.collection(EVENTS_COLLECTION_STRING)

    override fun getMainEvent(eventId: String): Outcome<EventModel, ErrorType> {
        val mainEventDocRef = firebaseDb
            .collection(EVENTS_COLLECTION_STRING)
            .document(eventId)
            .collection(MAIN_EVENT_COLLECTION_STRING)
            .document(MAIN_EVENT_DOCUMENT_ID)

        when (val mainEventSnapshot = queryDocument(context, mainEventDocRef)) {
            is Outcome.Success -> {
                mainEventSnapshot.value?.let { document ->
                    document.data?.let {
                        return if (document.isMainEventComplete())
                            Outcome.Success(document.getMainEvent())
                        else Outcome.Failure (ErrorType.NOT_FOUND, MESSAGE_ERROR_NOT_FOUND)
                    }
                }

                return Outcome.Failure(
                    ErrorType.NOT_FOUND,
                    MESSAGE_ERROR_NOT_FOUND,
                )
            }
            is Outcome.Failure ->
                return getOutcomeFailure(mainEventSnapshot.error, mainEventSnapshot.throwable)
        }
    }

    override fun getEventList(eventId: String): Outcome<List<EventModel>, ErrorType> {
        val documentRef = eventsDb.document(eventId)

        when (val scheduleSnapshot = queryDocument(context, documentRef)) {
            is Outcome.Success -> {
                scheduleSnapshot.value?.let { document ->
                    document.data?.let {
                        return if (document.isScheduleComplete())
                            Outcome.Success(document.toScheduleList())
                        else Outcome.Failure (ErrorType.NOT_FOUND, MESSAGE_ERROR_NOT_FOUND)
                    }
                }

                return Outcome.Failure(
                    ErrorType.NOT_FOUND,
                    MESSAGE_ERROR_NOT_FOUND,
                )
            }

            is Outcome.Failure ->
                return getOutcomeFailure(scheduleSnapshot.error, scheduleSnapshot.throwable)
        }
    }

    private fun getOutcomeFailure(error: ErrorType, throwable: Throwable?): Outcome.Failure<ErrorType> =
        Outcome.Failure(
            error,
            when (error) {
                ErrorType.NOT_FOUND -> MESSAGE_ERROR_NOT_FOUND
                ErrorType.CONNECTIVITY -> MESSAGE_ERROR_CONNECTIVITY
                ErrorType.GENERIC -> MESSAGE_ERROR_GENERIC
            },
            throwable,
        )

    fun queryDocument(context: Context, docRef: DocumentReference): Outcome<DocumentSnapshot, ErrorType> {
        if (!isInternetAvailable(context))
            return Outcome.Failure(ErrorType.CONNECTIVITY, MESSAGE_ERROR_CONNECTIVITY)

        val documentResult = try {
            docRef.get()
        } catch (error: IllegalStateException) {
            return Outcome.Failure(ErrorType.CONNECTIVITY, MESSAGE_ERROR_CONNECTIVITY, error)
        }

        if (!isResponseSuccessful(documentResult)) return Outcome.Failure(ErrorType.GENERIC, MESSAGE_ERROR_GENERIC, documentResult.exception)

        val document = documentResult.result
        if (document == null || !document.exists()) return Outcome.Failure(ErrorType.NOT_FOUND, MESSAGE_ERROR_NOT_FOUND, documentResult.exception)

        return Outcome.Success(document)
    }

    private fun queryDocumentList(context: Context, query: Query): Outcome<QuerySnapshot, ErrorType> {
        if (!isInternetAvailable(context))
            return Outcome.Failure(ErrorType.CONNECTIVITY, MESSAGE_ERROR_CONNECTIVITY)

        val queryResult = try {
            query.get()
        } catch (error: IllegalStateException) {
            return Outcome.Failure(ErrorType.CONNECTIVITY, MESSAGE_ERROR_CONNECTIVITY, error)
        }

        if (!isResponseSuccessful(queryResult)) return Outcome.Failure(ErrorType.GENERIC, MESSAGE_ERROR_GENERIC, queryResult.exception)

        val snapshot = queryResult.result
        if (snapshot == null || snapshot.isEmpty) return Outcome.Failure(ErrorType.NOT_FOUND, MESSAGE_ERROR_NOT_FOUND, queryResult.exception)

        return Outcome.Success(snapshot)
    }

    private fun <T> isResponseSuccessful(queryResult: Task<T>): Boolean {
        while (!queryResult.isComplete) {
            if (queryResult.isCanceled) return false
            Thread.sleep(50)
        }
        return queryResult.isSuccessful
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
