package com.jjim4963tw.kotlinapp

/**
 * var 為值可改變的變數、val 為值不可變的變數
 *
 * value?.length 為回傳值包含null
 * value?.length ?: 0 如為null就用?:後面的預設值
 * value!!.length為回傳值為null時丟出exception
 *
 *
 * 變數值加底線不會有任何效果，編譯會省略
 *
 * 變數型態與JAVA相同，用 to型態()  ex. toString()、toInt()、toDouble()...
 *
 * array: 不包含String
 *     1. val ages: IntArray = IntArray(3)
 *     2. val ages = intArrayOf(35, 17, 24)
 *
 * string array:
 *     1. val stringArray: Array<String?> = arrayOfNulls<String>(<元素個數>)
 *     2. val stringArray = arrayOfNulls<String>(<元素個數>)
 *     3. val stringArray: Array<String?> = arrayOfNulls(<元素個數>)
 *
 * 字串中可加 $變數名稱 直接帶入變數值
 *
 */
class VariableUtil {
    // variable
    var value: String = ""
    var valueIsNull: String? = null
    lateinit var valueNotInit: String

    val pi: Double = 3.14159265359
    val million: Int = 1_000_000

    // array
    val intArrayNotDefault :IntArray = IntArray(3)
    var intArrayNull :IntArray? = null
    val intArray :IntArray = intArrayOf(5, 6, 7)

    //string array
    var stringArrayNull :Array<String?>? = null
    var stringArray :Array<String?> = arrayOfNulls(3)
    var stringArrayDefault :Array<String?> = arrayOf("1", "2", "3")


    fun stringToInt(name: String): Int {
        return name.toInt();
    }

    fun stringToDouble(name: String? = null): Double {
        return name!!.toDouble()
    }

    fun printData(text : String) {
        print("Text = $text")
    }

    fun setIntArrayValue() {
        intArray[0] = 5
        intArray[1] = 6
        intArray[2] = 7
    }

    fun setStringArrayValue() {
        stringArrayNull = arrayOfNulls<String>(5)
    }
}