package com.scprojekt.infrastructure.processor

class CamelConstants {
    companion object {


        /* Properties */
        const  val JPA_URL: String = "jpaUrl"

        /* Routes */
        const val DIRECT_FINDBYUUID = "direct:findByUUID"
        const val DIRECT_FINDBYTYPE = "direct:findByType"
        const val DIRECT_FINDBYDESCRIPTION = "direct:findByDescription"
        const val DIRECT_FINDBYNAME = "direct:findByName"
        const val DIRECT_FINDBYID = "direct:findById"
        const val DIRECT_FINDALL = "direct:findAll"

        const val DIRECT_SAVEINDATABASE = "direct:saveInDatabase"
        const val DIRECT_DELETEINDATABASE = "direct:deleteInDatabase"
        const val DIRECT_DELETEALLINDATABASE = "direct:deleteAllInDatabase"
        const val DIRECT_UPDATEINDATABASE = "direct:updateInDatabase"

        const val DIRECT_DELETEALLUSERSINDATABASE = "direct:deleteAllUsersInDatabase"
        const val DIRECT_DELETEUSERINDATABASE = "direct:deleteUserInDatabase"
        const val DIRECT_UPDATEUSERINDATABASE = "direct:updateUserInDatabase"

        /* Logging */
        const val NODATABASE_URL = "No Database Url {0}"
    }

}
