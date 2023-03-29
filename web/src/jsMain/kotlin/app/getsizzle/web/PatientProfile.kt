package app.getsizzle.web

import app.getsizzle.shared.Api
import app.getsizzle.shared.Api.getSpecificPatient
import app.getsizzle.shared.convertSlashToStar
import app.getsizzle.shared.convertStarToSlash
import app.getsizzle.shared.data.model.Patient
import csstype.ClassName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.router.useLocation
import react.useEffectOnce
import react.useState

private val scope = MainScope()

val PatientProfile = FC<Props> {

    val patient = useLocation()
    var activePatient by useState(Patient("a", "a", "a", "a", 123, ""))
    var counter by useState(true)
    useEffectOnce {
        scope.launch {
            activePatient = getSpecificPatient(patient.search.substring(1))[0]
        }
    }

    console.log("please work: ${activePatient.fullName}")

    div {
        className = ClassName("image-text-container")
        div {
            className = ClassName("image-profile-container")
            img {
                src = "/pics/default_icon.png"
                alt = "default-profile"
            }
        }
        h3 {

            +activePatient.fullName
            span {
                className = ClassName("pencil")
                +" \t ✏️"
                onClick = {
                    counter = !counter
                }
            }
        }
        if (counter) {
            form {
                div {
                    className = ClassName("column-container")
                    div {
                        className = ClassName("left-column")
                        // add content for left column here
                        p {
                            +"Sex: ${activePatient.gender}"
                        }
                        p {
                            +"Age: "
                            if (activePatient.age != null) {
                                +"${activePatient.age}"
                            }
                        }
                        p {
                            +"Blood Pressure: "
                            if (activePatient.bloodPressure != null) {
                                +convertStarToSlash(activePatient.bloodPressure!!)
                            }
                        }
                    }
                    div {
                        className = ClassName("right-column")
                        // add content for right column here
                        p {
                            +"Weight: "
                            if (activePatient.weight != null) {
                                +"${activePatient.weight} lbs"
                            }

                        }
                        p {
                            +"Height: "
                            if (activePatient.height != null) {
                                +"${activePatient.height?.div(12)}' ${activePatient.height?.mod(12)}\""
                            }
                        }
                        p {
                            +"Allergens: "
                            if (activePatient.allergens != null) {
                                +convertStarToSlash(activePatient.allergens!!)
                            }
                        }
                    }
                }
                div {
                    className = ClassName("patient-notes")
                    div {
                        h4 {
                            +"LAST VISIT:"
                        }
                        p {
                            if (activePatient.lastVisit != null) {
                                +convertStarToSlash(activePatient.lastVisit!!)
                            }
                        }
                    }
                    div {
                        h4 {
                            +"CURRENT PRESCRIPTIONS:"
                        }
                        p {
                            if (activePatient.currentPrescriptions != null) {
                                +convertStarToSlash(activePatient.currentPrescriptions!!)
                            }
                        }
                    }
                    div {
                        h4 {
                            +"NOTES FROM LAST OBSERVATIONS:"
                        }
                        p {
                            if (activePatient.notes != null) {
                                +convertStarToSlash(activePatient.notes!!)
                            }
                        }
                    }
                }
            }
        }
        else if(!counter)
        {
            PatientFormComponent {
                onSubmit = { age, heightFeet, heightInches, weight, sex, bloodPressure, allergens, visit, prescriptions, notes ->
                    counter = !counter
                    scope.launch{
                        Api.updatePatientAge(activePatient.userId, age)
                        Api.updatePatientHeight(activePatient.userId, heightFeet * 12 + heightInches)
                        Api.updatePatientWeight(activePatient.userId, weight)
                        Api.updatePatientGender(activePatient.userId, sex)
                        Api.updatePatientBloodPressure(activePatient.userId, convertSlashToStar(bloodPressure))
                        Api.updatePatientAllergens(activePatient.userId, convertSlashToStar(allergens))
                        Api.updatePatientLastVisit(activePatient.userId, convertSlashToStar(visit))
                        Api.updatePatientPrescriptions(activePatient.userId, convertSlashToStar(prescriptions))
                        Api.updatePatientNotes(activePatient.userId, convertSlashToStar(notes))
                        activePatient = getSpecificPatient(patient.search.substring(1))[0]
                    }
                }
            }
        }
    }
}


