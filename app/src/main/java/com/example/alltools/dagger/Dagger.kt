package com.example.alltools.dagger

import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent {
    val computer: Computer
}

@Module
object AppModule {

    fun provideComputer(
        processor: Processor,
        motherboard: Motherboard,
        ram: RAM
    ): Computer {
        return Computer(
            processor = processor,
            ram = ram,
            motherboard = motherboard
        )
    }

    @Provides
    fun provideProcessor(): Processor {
        return Processor()
    }

    @Provides
    fun provideRAM(): RAM {
        return RAM()
    }

    @Provides
    fun provideMotherboard(): Motherboard {
        return Motherboard()
    }
}