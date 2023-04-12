package com.jjim4963tw.kotlinapp
/**
 * """..."""： 包含格式的文字使用此宣告
 * trimMargin： 刪除字串中邊界符號前的空白，可自定義符號
 *
 */
class StringProcessUtil {
    val textHasFormat: String = """
        |for (c in name) {
        |    print(c)
        |}
    """.trimMargin("|")

    companion object {
        // 判斷字串是否相同 (是否分大小寫)
        fun StringIsCommon(text1: String, text2: String, ignoreCase: Boolean): Boolean {
            if (ignoreCase) return text1.equals(text2, true)
            else            return text1.equals(text2)
        }

        // 判斷字串前面是否為指定的字串
        fun StringStartIsCommon(text: String, startText: String): Boolean {
            return text.startsWith(startText)
        }

        // 判斷字串結尾是否為指定的字串
        fun StringEndIsCommon(text: String, endText: String): Boolean {
            return text.endsWith(endText)
        }

        // 判斷字串是否空白
        fun StringIsBlank(text: String): Boolean {
            if (text.isBlank()) return text.isBlank()
            else                return text.isNotBlank()
        }

        // 判斷字串是否沒有字元
        fun StringIsEmpty(text: String): Boolean {
            if (text.isEmpty()) return text.isEmpty()
            else                return text.isNotEmpty()
        }

        // 計算字串裡有幾個字元
        fun getStringTotalIndex(text: String): Int {
            var count = 0
            for (i in text.indices) {
                count ++
            }
            return count
        }

        // 取得字串最後一個字元的編號
        fun getStringLastIndex(text: String): Int {
            return text.lastIndex
        }

        // 取得字串長度
        fun getStringLength(text: String): Int {
            return text.length
        }

        // 取得字串第一個字
        fun getStringFirstText(text: String): Char {
            return text.first()
        }

        // 取得字串最後一個字
        fun getStringLastText(text: String): Char {
            return text.last()
        }

        // 搜尋指定字串的位置編號
        fun searchStringIndex(text: String, searchText: String, isStart: Boolean): Int {
            if (isStart) return text.indexOf(searchText)
            else return text.lastIndexOf(searchText)
        }

        // 字串第一個字轉大寫
        fun changeTextCapitalize(text: String, onlyFirstText: Boolean): String {
            if (onlyFirstText) return text.capitalize()
            else return text.toUpperCase()
        }

        // 字串第一個字轉小寫
        fun changeTextDecapitalize(text: String, onlyFirstText: Boolean): String {
            if (onlyFirstText) return text.decapitalize()
            else return text.toLowerCase()
        }

        // 移除指定字串
        fun removeText(text: String, removeText: String): String {
            return text.removePrefix(removeText)
        }

        // 移除指定位置的字串
        fun removeIndexText(text: String, startIndex: Int, endIndex: Int): String {
            return text.removeRange(startIndex, endIndex)
        }
    }
}