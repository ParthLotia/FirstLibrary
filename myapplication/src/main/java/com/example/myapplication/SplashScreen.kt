package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

class SplashScreen : AppCompatActivity() {

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    var numberList: ArrayList<PhoneNumber> = ArrayList()
    var contactId: String = ""
    var name: String = ""
    var photo_stream: InputStream? = null
    var my_btmp: Bitmap? = null

    var image: Bitmap? = null
    var idContact: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.getString(key)
                Log.e("here", "Key: $key Value: $value")
                loadContacts()
            }
        }
    }

    private fun loadContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            getContacts()
//            listContacts.text = builder.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    @SuppressLint("Range")
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    if (cursorPhone!!.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            val contactId =
                                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                            builder.append("Contact: ").append(name).append(", Phone Number: ")
                                .append(
                                    phoneNumValue
                                ).append("\n\n")

                            val PhoneNumber = PhoneNumber()
                            PhoneNumber.number = phoneNumValue
                            PhoneNumber.id = contactId
                            numberList.add(PhoneNumber)
                            Log.e("Name ===>", phoneNumValue);
                        }
                    }

                    cursorPhone.close()
                }
            }
            checkData()
        } else {
            //   toast("No contacts available!")
            createNewContact()
        }
        cursor.close()
        return builder
    }

    private fun checkData() {
        if (numberList.size > 0) {


            for (i in 0 until numberList.size) {
//            1 (234) 567-890
                if (numberList[i].number.equals("(987) 654-3210", true)) {

                    editContact(numberList[i].number)
                    break
                } else {
                    createNewContact()

                    break
                }
            }
        }else{
            createNewContact()
        }
    }

    @SuppressLint("Range")
    private fun editContact(number: String) {
        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
        Log.e("editContact", "editContact")

        //Working

        /*
        val url = URL(Preferences().getImageUrl(this@SplashScreen).toString())
        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())


        val i = Intent(Intent.ACTION_INSERT_OR_EDIT)
        i.type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
        i.putExtra(ContactsContract.Intents.Insert.NAME, "name")
        i.putExtra(ContactsContract.Intents.Insert.PHONE, number)

        try {
            val row = ContentValues().apply {
                put(
                    ContactsContract.CommonDataKinds.Photo.PHOTO,
                    bitmapToByteArray(image)
                )
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                )
            }
            val data = arrayListOf(row)
            i.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)

        } catch (e: IOException) {
            Log.e("error", "" + e.message)
        }
        startActivity(i)*/


        //stackoverflow
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = applicationContext.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).equals(number,false)){
                idContact =
                    cursor!!.getLong(cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
            }

        }
        if (Preferences().getImageUrl(this@SplashScreen).toString().isEmpty()) {
            val url =
                URL("https://www.fedex.com/content/dam/fedex/us-united-states/shipping/images/2020/Q3/icon_delivery_purple_lg_2143296207.png")
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } else {
            val url = URL(Preferences().getImageUrl(this@SplashScreen).toString())
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }




        val i = Intent(Intent.ACTION_EDIT)
        val contactUri: Uri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContact!!)
        i.data = contactUri
        i.putExtra(ContactsContract.Intents.Insert.NAME, "name")
        try {
            val row = ContentValues().apply {
                put(
                    ContactsContract.CommonDataKinds.Photo.PHOTO,
                    bitmapToByteArray(image)
                )
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                )
            }
            val data = arrayListOf(row)
            i.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)

        } catch (e: IOException) {
            Log.e("error", "" + e.message)
        }
        i.putExtra("finishActivityOnSaveCompleted", true)
        startActivity(i)
        finish()



        //Android Document

        /*var mCursor: Cursor? = null
        // The index of the lookup key column in the cursor
        var lookupKeyIndex: Int = 0
        // The index of the contact's _ID value
        var idIndex: Int = 0
        // The lookup key from the Cursor
        var currentLookupKey: String? = null
        // The _ID value from the Cursor
        var currentId: Long = 0
        // A content URI pointing to the contact
        var selectedContactUri: Uri? = null

        mCursor?.apply {
            // Gets the lookup key column index
            lookupKeyIndex = getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            // Gets the lookup key value
            currentLookupKey = getString(lookupKeyIndex)
            // Gets the _ID column index
            idIndex = getColumnIndex(ContactsContract.Contacts._ID)
            currentId = getLong(idIndex)
//            selectedContactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey)
            val mContactstr =
                mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
            selectedContactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                mContactstr.toLong()
            )


            val editIntent = Intent(Intent.ACTION_EDIT).apply {
                setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
            }
            editIntent.setDataAndType(selectedContactUri,ContactsContract.Contacts.CONTENT_ITEM_TYPE)
            editIntent.putExtra(ContactsContract.Intents.Insert.NAME, name)
            editIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number)
            editIntent.putExtra("finishActivityOnSaveCompleted", true)
            editIntent.addFlags(editIntent.flags)
            startActivityForResult(editIntent, 2)
        }*/

    }

    private fun createNewContact() {
        Log.e("createContact", "createContact")


        val intent =
            Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
                type = ContactsContract.RawContacts.CONTENT_TYPE
                putExtra(ContactsContract.Intents.Insert.NAME, "test")
                putExtra(ContactsContract.Intents.Insert.EMAIL, "test@gmail.com")
                putExtra(ContactsContract.Intents.Insert.PHONE, "7575078895")

                try {
                    val url = URL(Preferences().getImageUrl(this@SplashScreen).toString())
                    val image =
                        BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    val row = ContentValues().apply {
                        put(
                            ContactsContract.CommonDataKinds.Photo.PHOTO,
                            bitmapToByteArray(image)
                        )
                        put(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                        )
                    }
                    val data = arrayListOf(row)

                    putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
                } catch (e: IOException) {
                    println(e)
                }
            }
        startActivity(intent)
        finish()


    }

}