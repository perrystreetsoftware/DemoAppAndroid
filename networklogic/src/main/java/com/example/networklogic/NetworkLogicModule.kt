import com.example.interfaces.ITravelAdvisoriesApi
import com.example.networklogic.TravelAdvisoriesApi
import org.koin.dsl.module

val networkLogicApi = module {
    factory<ITravelAdvisoriesApi> {
        TravelAdvisoriesApi(
            moshi = get()
        )
    }
}
