package app.getsizzle.web

import app.getsizzle.shared.Api.getSpecificPatient
import app.getsizzle.shared.convertStarToSlash
import csstype.ClassName
import csstype.px
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.textarea
import react.router.useLocation
import react.useEffectOnce
import react.useState

private val scope = MainScope()

external interface PatientFormProps : Props {
    var onSubmit: (Int, Int, Int, Double, String, String, String, String, String, String) -> Unit
}

val PatientFormComponent = FC<PatientFormProps> { props ->

    val patient = useLocation()

    val (age, setAge) = useState(21)
    val (heightFeet, setHeightFeet) = useState(5)
    val (heightInches, setHeightInches) = useState(5)
    val (weight, setWeight) = useState(155.toDouble())
    val (sex, setSex) = useState("")
    val (bloodPressure, setBloodPressure) = useState("")
    val (allergens, setAllergens) = useState("")
    val (visit, setVisit) = useState("")
    val (prescriptions, setPrescriptions) = useState("")
    val (notes, setNotes) = useState("")

    useEffectOnce {
        scope.launch {
            getSpecificPatient(patient.search.substring(1))[0].age?.let { setAge(it) }
            getSpecificPatient(patient.search.substring(1))[0].height?.let { setHeightFeet(it.div(12)) }
            getSpecificPatient(patient.search.substring(1))[0].height?.let { setHeightInches(it.mod(12)) }
            getSpecificPatient(patient.search.substring(1))[0].weight?.let { setWeight(it) }
            setSex(getSpecificPatient(patient.search.substring(1))[0].gender.toString())
            getSpecificPatient(patient.search.substring(1))[0].bloodPressure?.let { setBloodPressure(it) }
            getSpecificPatient(patient.search.substring(1))[0].bloodPressure?.let { setBloodPressure(it) }
            getSpecificPatient(patient.search.substring(1))[0].allergens?.let { setAllergens(it) }
            getSpecificPatient(patient.search.substring(1))[0].lastVisit?.let { setVisit(it) }
            getSpecificPatient(patient.search.substring(1))[0].currentPrescriptions?.let { setPrescriptions(it) }
            getSpecificPatient(patient.search.substring(1))[0].notes?.let { setNotes(it) }


        }
    }




    val submitHandler: FormEventHandler<HTMLFormElement> = { // on submit do this
        it.preventDefault() // prevents autosubmit or something
        setAge(-1)
        setHeightFeet(-1)
        setHeightInches(-1)
        setWeight(-0.1)
        setSex("")
        setBloodPressure("")
        setAllergens("")
        setVisit("")
        setPrescriptions("")
        setNotes("")
        props.onSubmit(
            age,
            heightFeet,
            heightInches,
            weight,
            sex,
            bloodPressure,
            allergens,
            visit,
            prescriptions,
            notes
        )
    }

    val changeHandlerAge: ChangeEventHandler<HTMLInputElement> = {
        setAge(it.target.value.toInt())
    }

    val changeHandlerHeightFeet: ChangeEventHandler<HTMLInputElement> = {
        setHeightFeet(it.target.value.toInt())
    }
    val changeHandlerHeightInches: ChangeEventHandler<HTMLInputElement> = {
        setHeightInches(it.target.value.toInt())
    }
    val changeHandlerWeight: ChangeEventHandler<HTMLInputElement> = {
        setWeight(it.target.value.toDouble())
    }
    val changeHandlerSexSelect: ChangeEventHandler<HTMLSelectElement> = {
        setSex(it.target.value)
    }
    val changeHandlerBloodPressure: ChangeEventHandler<HTMLInputElement> = {
        setBloodPressure(it.target.value)
    }
    val changeHandlerAllergens: ChangeEventHandler<HTMLInputElement> = {
        setAllergens(it.target.value)
    }
    val changeHandlerVisit: ChangeEventHandler<HTMLTextAreaElement> = {
        setVisit(it.target.value)
    }
    val changeHandlerPrescriptions: ChangeEventHandler<HTMLTextAreaElement> = {
        setPrescriptions(it.target.value)
    }
    val changeHandlerNotes: ChangeEventHandler<HTMLTextAreaElement> = {
        setNotes(it.target.value)
    }


    form {
        onSubmit = submitHandler
        div {
            className = ClassName("profile-flex")
            div {
                className = ClassName("column-container")
                div {
                    className = ClassName("left-column")
                    p {
                        +"Sex: "
                        select {
                            name = "selectedSex"
                            value = sex
                            id = "submit3"
                            option {
                                value = "MALE"
                                +"MALE"
                            }
                            option {
                                value = "FEMALE"
                                +"FEMALE"
                            }
                            option {
                                value = "TRANSGENDER"
                                +"TRANSGENDER"
                            }
                            option {
                                value = "NONBINARY"
                                +"NONBINARY"
                            }
                            option {
                                value = "UNSPECIFIED"
                                +"UNSPECIFIED"
                            }

                            onChange = changeHandlerSexSelect
                        }
                    }
                    p {
                        +"Age: "
                        input {
                            type = InputType.number
                            min = "1".toDouble()
                            max = "100".toDouble()
                            name = "age"
                            value = age.toString()
                            id = "submit3"
                            onChange = changeHandlerAge
                        }
                    }
                    p {
                        +"Blood Pressure: "
                        input {
                            type = InputType.text
                            onChange = changeHandlerBloodPressure
                            name = "bloodPressure"
                            value = convertStarToSlash(bloodPressure)
                            placeholder = ""
                            id = "submit3"
                        }
                    }
                }
                div {
                    className = ClassName("right-column")
                    p {
                        +"Weight: "
                        input {
                            type = InputType.number
                            min = "1".toDouble()
                            max = "1000".toDouble()
                            name = "weight"
                            value = weight.toString()
                            onChange = changeHandlerWeight
                            id = "submit3"
                        }
                        +"\tlbs"
                    }
                    p {
                        +"Height: "
                        input {
                            type = InputType.number
                            min = "0".toDouble()
                            max = "8".toDouble()
                            name = "feet"
                            onChange = changeHandlerHeightFeet
                            value = heightFeet.toString()
                            id = "submit3"
                        }
                        +"\t'\t"
                        input {
                            type = InputType.number
                            min = "0".toDouble()
                            max = "11".toDouble()
                            name = "inches"
                            value = heightInches.toString()
                            onChange = changeHandlerHeightInches
                            id = "submit3"
                        }
                        +"\t\""
                    }
                    p {
                        +"Allergens: "
                        input {
                            type = InputType.text
                            onChange = changeHandlerAllergens
                            name = "allergens"
                            value = convertStarToSlash(allergens)
                            placeholder = ""
                            id = "submit3"
                        }
                    }
                }
            }

            div {
                className = ClassName("patient-notes")
                div {

                    h4 {
                        css { marginTop = 4.px }
                        +"LAST VISIT:"
                    }
                    textarea {
                        id = "input-text-area"
                        value = convertStarToSlash(visit)
                        onChange = changeHandlerVisit
                    }
                    h4 {
                        +"CURRENT PRESCRIPTION:"
                    }
                    textarea {
                        id = "input-text-area"
                        value = convertStarToSlash(prescriptions)
                        onChange = changeHandlerPrescriptions
                    }
                    h4 {
                        +"NOTES FROM LAST OBSERVATION: "
                    }
                    textarea {
                        id = "input-text-area"
                        value = convertStarToSlash(notes)
                        onChange = changeHandlerNotes
                    }
                }
            }
        }

        div {
            className = ClassName("saveInfoFormContainer")
            input {
                className = ClassName("saveInfoForm")
                type = InputType.submit
                value = "Save"
            }
        }
    }
}