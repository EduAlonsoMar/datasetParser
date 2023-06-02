package ui.css

import javafx.geometry.Pos
import javafx.scene.paint.Paint
import tornadofx.*
import java.io.File

class AppStyle: Stylesheet() {

    companion object {
        val exploreButton by cssclass()

        val appScreen by cssclass()

        val titleDiv by cssclass()

        val titleLabel by cssclass()

        val regularText by cssclass()

        val textField by cssclass()

        val comboBoxWithLimit by cssclass()

        val parserLine by cssclass()

        val parserGroup by cssclass()

        val divider by cssclass()

        val verticalSeparator by cssclass()

        val verticalLayoutWithBorder by cssclass()

        val containerWithPadding by cssclass()

        val resultScreen by cssclass()
    }

    init {
        exploreButton {
            backgroundColor += Paint.valueOf("#D5F5E3")

        }

        appScreen {
            //minWidth = Dimension(1080.0, Dimension.LinearUnits.px)
            //minHeight = Dimension(750.0, Dimension.LinearUnits.px)
            backgroundColor += Paint.valueOf("#AAB7B8")
            // backgroundImage += File("/Users/edualonso/IdeaProjects/FakeNewsDataSetParser/FakeNewsDataSetParser/src/main/resources/Images/background1.jpg").toURI()
        }

        resultScreen {
            backgroundColor += Paint.valueOf("#E9F7EF")
        }

        parserLine {
            padding = CssBox(Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px) )
        }

        parserGroup {
            padding = CssBox(Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px) )
        }

        divider {
            backgroundColor += Paint.valueOf("#17202A")
            borderColor += CssBox(Paint.valueOf("#17202A"), Paint.valueOf("#17202A"), Paint.valueOf("#17202A"), Paint.valueOf("#17202A"))
        }

        verticalLayoutWithBorder {
            borderColor += CssBox(Paint.valueOf("#17202A"), Paint.valueOf("#17202A"), Paint.valueOf("#17202A"), Paint.valueOf("#17202A"))
            borderWidth = MultiValue(arrayOf(CssBox(Dimension(5.0, Dimension.LinearUnits.px), Dimension(5.0, Dimension.LinearUnits.px), Dimension(5.0, Dimension.LinearUnits.px), Dimension(5.0, Dimension.LinearUnits.px))))
            borderRadius = MultiValue(arrayOf(CssBox(Dimension(20.0, Dimension.LinearUnits.px), Dimension(20.0, Dimension.LinearUnits.px), Dimension(20.0, Dimension.LinearUnits.px), Dimension(20.0, Dimension.LinearUnits.px))))
        }

        containerWithPadding {
            padding = CssBox(Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px), Dimension(10.0, Dimension.LinearUnits.px) )
        }

        titleDiv {
            alignment = Pos.TOP_CENTER
        }

        titleLabel {
            fontSize = Dimension(50.0, Dimension.LinearUnits.px)
        }

        regularText {
            fontSize = Dimension(15.0, Dimension.LinearUnits.px)
            minWidth = Dimension(250.0, Dimension.LinearUnits.px)
        }

        textField {
            minWidth = Dimension(300.0, Dimension.LinearUnits.px)
        }

        comboBoxWithLimit {
            maxWidth = Dimension(300.0, Dimension.LinearUnits.px)
        }

        verticalSeparator {
            minHeight = Dimension(10.0, Dimension.LinearUnits.px)
        }
    }

}