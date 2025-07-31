    package com.example.petcaring.data.repository

    import android.content.Context
    import com.example.petcaring.data.db.AppDatabase
    import com.example.petcaring.data.model.User

    class UserRepository(context: Context) {
        private val userDao = AppDatabase.getInstance(context).userDao()

        suspend fun register(user: User) = userDao.register(user)

        suspend fun login(email: String, password: String): User? =
            userDao.login(email, password)

        suspend fun updateUser(user: User) {
            userDao.updateUser(user)
        }
        suspend fun getUserByEmail(email: String): User? {
            return userDao.getUserByEmail(email)
        }

    }
