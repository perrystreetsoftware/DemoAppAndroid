import com.example.interfaces.TravelApiImplementing
import com.example.networklogic.TravelAdvisoriesApi
import org.koin.dsl.module

val networkLogicApi = module {
    factory<TravelApiImplementing> {
        TravelAdvisoriesApi(
            moshi = get()
        )
    }
}
