package app.getsizzle.web

import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.useState

external interface SearchProp: Props {
    var onSubmit: (String) -> Unit
}

val SearchComponent = FC<SearchProp> { props ->

    val (search, setSearch) = useState("")
    val changeHandlerSearch: ChangeEventHandler<HTMLInputElement> = {
        props.onSubmit(it.target.value)
        setSearch("")
    }
    input {
        type = InputType.search
        id = "search_bar"
        onChange = changeHandlerSearch
        name = "search"
        placeholder = "Search Patient"

    }
}