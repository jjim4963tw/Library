package com.jjim4963tw.library.layout.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Component
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.*

class DaggerActivity : AppCompatActivity() {
    private lateinit var honeyLemonade: HoneyLemonade

    // 為了讓Dagger可存取並設值，此參數不可設為private
    @Inject
    lateinit var injectionHoneyLemonade: HoneyLemonade

    @Inject
    lateinit var lazy: Lazy<HoneyLemonade>
    @Inject
    lateinit var provider: Provider<HoneyLemonade>

    @Inject
    @LemonFlavor
    lateinit var honeyLemon: HoneyLemonade
    @Inject
    @LymeFlavor
    lateinit var honeyLyme: HoneyLemonade

    override fun onCreate(savedInstanceState: Bundle?) {
        // 透過傳入Activity的實體，讓Dagger自動把有加註@Inject 的變數設值，此時 injectionHoneyLemonade 才會有值
        DaggerHoneyLemonadeComponent.create().inject(this)
        super.onCreate(savedInstanceState)

        // 透過DaggerInterfaceName取得值
        val component = DaggerHoneyLemonadeComponent.create()
        honeyLemonade = component.getHoneyLemonade()

        // 當呼叫get()時，真正的實體才會被建立，可在任何需要的地方要求Dagger注入一個Lazy、Provider
        val lazyIsCommon = lazy.get() == lazy.get() // true
        val providerIsCommon = provider.get() == provider.get() // false
    }
}

//region Inject Class
/**
 * @Inject：代表該物件為injectable的，Dagger利用此來了解如何提供所需要的dependency
 * 如有多個constructor，只能選擇一個加 @Inject。
 * injectable 的物件中的建構子參數與必須為injectable
 */
class HoneyLemonade @Inject constructor(val honey: Honey, val lemon: Lemon)

class Honey @Inject constructor(val bee: Bee)

class Bee @Inject constructor()

open class Lemon

class Lyme @Inject constructor(val water: Water) : Lemon()

class Water @Inject constructor()
//endregion

//region Component
/**
 * @Component：負責提供Dependency的入口
 */
@Component(modules = [HoneyLemonadeModule::class])
interface HoneyLemonadeComponent {
    fun getHoneyLemonade(): HoneyLemonade

    fun inject(activity: DaggerActivity)
}
//endregion

//region Module & Provide & Binds
/**
 * @Module：宣告Class是Dagger提供dependency對照的物件
 * @Provides：提供回傳型別dependency的function
 * 藉此達成變換dependency的同時，不需要改變所有使用的地方
 */
@Module
class HoneyLemonadeModule {
    @Provides
    fun provideLemon(lyme: Lyme): Lemon {
        return lyme
    }

    @Provides
    @LemonFlavor
    fun provideHoneyLemon(honey: Honey, lemon: Lemon): HoneyLemonade {
        return HoneyLemonade(honey, lemon)
    }

    @Provides
    @LymeFlavor
    fun provideHoneyLyme(honey: Honey, lyme: Lyme): HoneyLemonade {
        return HoneyLemonade(honey, lyme)
    }
}

/**
 * Binds
 */
@Module
abstract class HoneyLemonadeBinds {
    @Binds
    abstract fun provideLemon(lyme: Lyme): Lemon
}
//endregion

//region Scope
/**
 * @Scope：識別不同生命週期，如在@Module加入
 */
@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FragmentScope
//endregion

//region Named & Qualifier
/**
 * @Named("value")：區分相同回傳型別的差異，讓Dagger知道兩者為不同物件
 * @Qualifier：直接將原本@Named("value")的Value宣告為annotation，減少錯字
 */
@Qualifier
annotation class LemonFlavor

@Qualifier
annotation class LymeFlavor
//endregion
