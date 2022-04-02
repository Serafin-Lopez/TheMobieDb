package mx.com.developer.themobiedb.communication

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val statusResponse : Int = -1) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T, statusResponse: Int = -1): Resource<T> {
            return Resource(Status.SUCCESS, data, null , statusResponse)
        }

        fun <T> success(data: T ): Resource<T> {
            return Resource(Status.SUCCESS, data, null )
        }

        fun <T> error(message: String, statusResponse: Int = -1, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message, statusResponse)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, -1)
        }

        fun <T> loading(data: T? = null, message: String): Resource<T> {
            return Resource(Status.LOADING, data, message, -1)
        }
    }

}