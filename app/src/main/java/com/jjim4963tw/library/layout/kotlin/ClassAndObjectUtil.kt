package com.jjim4963tw.kotlinapp

/**
 *
 * constructor(var id: Long, ...)：宣告建構式時同時宣告其中變數，如有給預設值則不用每次呼叫class都須給所有參數
 * constructor(var id: Long = -1, ...)：給預設值則不用每次呼叫class都須給所有參數
 * constructor(_id: Long = -1, ...) {  手動宣告變數
 *      var id = _id
 *      ...
 * }
 *
 *      val classAndObjectUtil = ClassAndObjectUtil(10024, title)
 *      val classAndObjectUtil = ClassAndObjectUtil(10024)
 *
 * constructor(參數名稱: 參數型態,...): this(...) {    在建立物件的時候，執行一些額外工作
 *      // 建構式執行的工作
 * }
 *
 * set(value: String) {}  get() {}：手動更改getter setter
 *
 */
class ClassAndObjectUtil constructor(var id: Long = -1,
                                     var title: String = "",
                                     text: String = "") {

    var _text = text
        set(value: String) {
            if (StringProcessUtil.StringIsEmpty(value)) {
                field = value
            }
        }

        get() {
            return if (StringProcessUtil.StringIsEmpty(field)) "Empty" else field
        }

    constructor(id: Long) : this(id, "")
}