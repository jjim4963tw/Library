package com.jjim4963tw.kotlinapp

/**
 *
 * open class/fun name {} 可繼承的class或fun，須在前面加上open
 *      繼承方式： 1. class name1(參數名稱: 參數型態,..., val|var 屬性名稱: 屬性型態): name(參數,...)
 *               2. override fun funName
 * super.name 執行父類別
 */
open class ExtendsUtil {

    open fun getString() {}
}