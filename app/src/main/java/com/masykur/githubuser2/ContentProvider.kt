package com.masykur.githubuser2

import android.content.*
import android.content.ContentProvider
import android.database.Cursor
import android.net.Uri
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item

class ContentProvider : ContentProvider() {

    companion object {

        /** The authority of this content provider */
        private const val AUTHORITY = "com.masykur.githubuser2"

        /** The match code for some items in the Cheese table */
        private const val CODE_USER_PROVIDER_DIR = 1

        /** The match code for an items in the Cheese table */
        private const val CODE_USER_PROVIDER_ITEM = 2

        val URI_USER = Uri.parse("content://$AUTHORITY/${Item.TABLE_NAME}")
        private lateinit var userDetail: UserDetail

        /**
         * Add Uri to match. The code is used to check whenever the Uri is matched.
         * Use * for any text, # for only numbers.
         *
         * @param authority: the authority to match. (quyền để khớp)
         * @param path: The path to match. May use * for any text, # for only number
         * @param code: The code that is return when a Uri is matched against the given components.
         */
        val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, Item.TABLE_NAME, CODE_USER_PROVIDER_DIR)
            addURI(AUTHORITY, Item.TABLE_NAME + "/*", CODE_USER_PROVIDER_ITEM)
        }
    }



    override fun getType(uri: Uri): String =
        when (MATCHER.match(uri)) {
            CODE_USER_PROVIDER_DIR -> "vnd.android.cursor.dir/$AUTHORITY.${Item.TABLE_NAME}"
            CODE_USER_PROVIDER_ITEM -> "vnd.android.cursor.item/$AUTHORITY.${Item.TABLE_NAME}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    override  fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (CODE_USER_PROVIDER_DIR) {
            MATCHER.match(uri) -> userDetail.getUserDetail().upsertByUri(Item.fromContentValues(contentValues))
            else -> 0
        }
        context?.contentResolver?.notifyChange(URI_USER, null)
        return Uri.parse("$URI_USER/$added")
    }



    override  fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (CODE_USER_PROVIDER_ITEM) {
            MATCHER.match(uri) -> userDetail.getUserDetail().deleteByIdUri(ContentUris.parseId(uri))
            else -> 0
        }
        context?.contentResolver?.notifyChange(URI_USER, null)
        return deleted
    }
    override fun onCreate(): Boolean {
         userDetail = UserDetail.invoke(context as Context)
        userDetail.getUserDetail()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        val cursor: Cursor?

        when (MATCHER.match(uri)) {
            CODE_USER_PROVIDER_ITEM -> cursor = userDetail.getUserDetail().selectById(ContentUris.parseId(uri))
            CODE_USER_PROVIDER_DIR -> cursor = userDetail.getUserDetail().getAllUsersByUri()
            else -> cursor = null
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
