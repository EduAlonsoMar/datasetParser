package koin

import data.database.FakeNewsDataBase
import data.datasource.*
import org.koin.dsl.module


val appModule = module {

    single {
        FakeNewsDataBase()
    }

    single {
        DatasetLabeledDataSource()
    }

    single {
        DatasetNotLabeledDataSource()
    }

    single {
        ExecutionsDataSource()
    }

    single {
        DatasetLabeledParserDataSource()
    }

    single {
        DatasetNotLabeledParserDataSource()
    }

    single {
        ExecutionsParserDataSource()
    }

}