package com.jjim4963tw.kotlinapp

/**
 *
 * 單一函數運算式: fun 函式名稱(參數名稱:參數型態 = 預設值,...) = 運算式
 * 需有回傳值的方法: fun 方法名稱(變數名稱 : 變數型態): 回傳值型態 {}
 * 不需有回傳值的方法: fun 方法名稱(): Unit {}
 *
 * companion object {}: 為 java 的 "static"
 */
class FunctionInitUtil {
    companion object {
        fun getHellWorld() = "Hello World!"

        fun stringToInt(name: String): Int {
            return name.toInt();
        }

        // 如未傳值，則預設name = null
        fun stringToDouble(name: String? = null): Double? {
            return name?.toDouble()
        }

        fun notReturnAndInputValue() {
            print("notReturnAndInputValue")
        }

        fun notReturnAndInputValue1(): Unit {
            print("notReturnAndInputValue")
        }

        // 函數多載時變數個數不固定時可用 vararg，如有其他參數則需要宣告在最後一個
        // 可呼叫 average(1, 2, 3)、average(1, 2, 3, 4, 5)
        fun average(message: String, vararg ns: Int): Int {
            var total = 0;
            for (n in ns) {
                total += n
            }
            print("$message: $total")
            return total / ns.size
        }
    }
}