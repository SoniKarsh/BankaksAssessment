package com.codehustlers.bankaks.data

data class ResTaskFormModel(
    val message: String,
    val result: Result,
    val status: String
) {
    data class Result(
        val fields: List<Field>,
        val number_of_fields: Int,
        val operator_name: String,
        val screen_title: String,
        val service_id: String
    ) {
        data class Field(
            val hint_text: String,
            val is_mandatory: String,
            val name: String,
            val placeholder: String,
            val regex: String,
            val type: Type,
            val ui_type: UiType
        ) {
            data class UiType(
                val type: String,
                val values: List<Value>
            ) {
                data class Value(
                    val id: String,
                    val name: String
                )
            }
            data class Type(
                val array_name: String,
                val data_type: String,
                val is_array: String
            )
        }
    }
}