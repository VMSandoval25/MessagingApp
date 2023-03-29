//
//  SignUpView.swift
//  messaging
//
//  Created by Howie Nguyen on 1/24/23.
//

import Foundation
import SwiftUI
import shared
import KMPNativeCoroutinesRxSwift
import RxSwift


let lightGreyColor2 = Color(red: 239.0/255.0, green: 243.0/255.0, blue: 244.0/255.0, opacity: 1.0)
let darkOrange2 = Color(red: 227/255.0, green: 84/255.0, blue: 21/255.0, opacity: 1.0)

struct SignUpView: View {
    @ObservedObject private var vm = SignUpViewModel()
        @State private var email = ""
        @State private var userID = ""
        @State private var password = ""
        @State private var fullname = ""
        @State private var dietitianID = ""
        @State private var age = 0
       @State private var height = 0
      @State private var weight = 0.0
    @State private var gender = Gender.unspecified
    var body: some View {
        ScrollView{
        VStack() {
            
          
                
               // SignUpText()
                
                GenderCheckField(gender: $gender)
                FullnameCheckField(text: $fullname, placeHolder: "Full Name")
                UserTextField(text: $userID, placeHolder: "User Name")
            PasswordCheckField(text: $password, placeHolder: "Password")
                EmailCheckField(text: $email, placeHolder: "Email")
                AgeCheckField(number: $age, placeholder: "Age")
           
                HeightCheckField(heightInInches: $height, feetPlaceholder: "ft", inchesPlaceholder: "in")
                WeightCheckField(value: $weight, placeholder: "lbs")
                
                //WeightPicker(weightInLbs: $weight)
                DietitianIDCheckField(text:$dietitianID, placeHolder:"Dietitian Email")
                
                
                Button(action: {
                    vm.userID = userID
                    vm.password = password
                    vm.email = email
                    vm.fullname = fullname
                    vm.dietitianID = dietitianID
                    vm.age = age
                    vm.height = height
                    vm.weight = weight
                    vm.gender = gender
                    
                    
                    vm.register(fullName: fullname, userId: userID, password: password, email: email, dietitianID: dietitianID, age: age, height: height, weight: weight, gender: gender)
                    
                })
                {
                    SignUpButtonContent(title: "Sign up")
                    
                }
                
                
                
            }
        .padding(.top, 20)
            
            
        }.padding([.leading, .trailing], 27.5)
           
        
            .toolbarBackground(
                        // Color(red: 0.883, green: 0.833, blue: 0.883, opacity: 0.12)
                        Color(red: 1, green: 0.933, blue: 0.902, opacity: 1),
                        for: .navigationBar)
                    .toolbarBackground(.visible, for: .navigationBar)
                    .navigationTitle("Register")

    }
        
}


struct UserTextField: View{
    @Binding var text: String
    var placeHolder: String
    
    @State private var isEditing = false // track if the TextField is active
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            TextField(placeHolder,text: $text, onEditingChanged: { editing in
                self.isEditing = editing // update isEditing when the TextField is active/inactive
            })
            .autocapitalization(.none)
            .frame(height: 53) // set a fixed height for the text field
            .padding(.horizontal, 10) // add some padding to the left and right of the text field
            //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
            .foregroundColor(.black)
            
        }
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.bottom, 5)
        .overlay(
            // Add an orange underline if the TextField is active
            RoundedRectangle(cornerRadius: 10)
                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .offset(y: 26) // adjust the position of the underline
        )
    }
}

struct PasswordCheckField: View {
    @Binding var text: String
    var placeHolder: String
    
    @FocusState private var isFocused: Bool // track if the SecureField is active
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            SecureField(placeHolder, text: $text)
                .focused($isFocused)
                .frame(height: 53) // set a fixed height for the text field
                .padding(.horizontal, 10) // add some padding to the left and right of the text field
                .background(Color.clear)
                //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
                .foregroundColor(.black)
                .opacity(isFocused ? 0.5 : 1.0) // add opacity animation
                
        }
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.bottom, 5)
        .overlay(
            // Add an orange underline if the SecureField is active
            RoundedRectangle(cornerRadius: 10)
                .fill(isFocused ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .offset(y: 26) // adjust the position of the underline
        )
        .animation(.easeInOut(duration: 0.2)) // add animation for smoother transition
        .onTapGesture {
            isFocused = true
        }
        .onSubmit {
            isFocused = false
        }
    }
}




    struct EmailCheckField: View {
        @Binding var text: String
        var placeHolder: String
        
        @State private var isEditing = false // track if the TextField is active
        
        var body: some View {
            ZStack {
                RoundedRectangle(cornerRadius: 10)
                    .fill(Color.white)
                    .shadow(radius: 5)
                
                TextField(placeHolder,text: $text, onEditingChanged: { editing in
                    self.isEditing = editing // update isEditing when the TextField is active/inactive
                })
                .autocapitalization(.none)
                .frame(height: 53) // set a fixed height for the text field
                .padding(.horizontal, 10) // add some padding to the left and right of the text field
                //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
                .foregroundColor(.black)
                
            }
            .frame(width: 230) // set a fixed width for the text field and background
            .padding(.bottom, 5)
            .overlay(
                // Add an orange underline if the TextField is active
                RoundedRectangle(cornerRadius: 10)
                    .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                    .frame(height: 2)
                    .offset(y: 26) // adjust the position of the underline
            )
        }
    }




struct FullnameCheckField: View{
    @Binding var text: String
    var placeHolder: String
    
    @State private var isEditing = false // track if the TextField is active
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            TextField(placeHolder,text: $text, onEditingChanged: { editing in
                self.isEditing = editing // update isEditing when the TextField is active/inactive
            })
            .autocapitalization(.none)
            .frame(height: 53) // set a fixed height for the text field
            .padding(.horizontal, 10) // add some padding to the left and right of the text field
            //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
            .foregroundColor(.black)
            
        }
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.bottom, 5)
        .overlay(
            // Add an orange underline if the TextField is active
            RoundedRectangle(cornerRadius: 10)
                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .offset(y: 26) // adjust the position of the underline
        )
    }
    
}


struct AgeCheckField: View {
    @Binding var number: Int
    var placeholder: String

    @State private var isEditing = false // track if the TextField is active

    var body: some View {
        let text = Binding<String>(
            get: {
                if number == 0 {
                    return ""
                } else {
                    return String(number)
                }
            },
            set: {
                if let value = Int($0) {
                    number = value
                }
            }
        )

        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            TextField(placeholder, text: text, onEditingChanged: { editing in
                self.isEditing = editing // update isEditing when the TextField is active/inactive
            })
            .frame(height: 53) // set a fixed height for the text field
            .padding(.horizontal, 10) // add some padding to the left and right of the text field
            //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
            .foregroundColor(.black)
            
        }
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.bottom, 5)
        .overlay(
            // Add an orange underline if the TextField is active
            RoundedRectangle(cornerRadius: 10)
                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .offset(y: 26) // adjust the position of the underline
        )
        .keyboardType(.numberPad) // Use the number pad keyboard for this TextField
    }
}
struct HeightCheckField: View {
    @Binding var heightInInches: Int
    var feetPlaceholder: String
    var inchesPlaceholder: String

    @State private var isEditingFeet = false // track if the feet TextField is active
    @State private var isEditingInches = false // track if the inches TextField is active

    var body: some View {
        let feet = Binding<Int>(
            get: { heightInInches / 12 },
            set: {
                let inches = $0 * 12 + (heightInInches % 12)
                heightInInches = inches
            }
        )

        let inches = Binding<Int>(
            get: { heightInInches % 12 },
            set: {
                let newHeightInInches = feet.wrappedValue * 12 + $0
                heightInInches = newHeightInInches
            }
        )

        HStack(spacing: 10) {
            // TextField for feet
            ZStack(alignment: .bottom) {
                RoundedRectangle(cornerRadius: 5.0)
                    .fill(Color.white)
                    .frame(width: 110, height: 53)
                    .shadow(radius: 5)
                TextField(feetPlaceholder, text: Binding<String>(
                    get: { feet.wrappedValue == 0 ? "" : String(feet.wrappedValue) },
                    set: { feet.wrappedValue = Int($0) ?? 0 }
                ), onEditingChanged: { editing in
                    self.isEditingFeet = editing
                })
                .frame(width: 90, height: 53) // set a fixed size for the text field
                .padding(.horizontal, 2) // add some padding to the left and right of the text field
                .background(Color.clear)
                .keyboardType(.numberPad)
            }
            .background(lightGreyColor)
            .overlay(
                // Add an orange underline if the TextField is active
                Rectangle()
                    .fill(isEditingFeet ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                    .frame(height: 2)
                    .offset(y: 27) // adjust the position of the underline
            )

            // TextField for inches
            ZStack(alignment: .bottom) {
                RoundedRectangle(cornerRadius: 5.0)
                    .fill(Color.white)
                    .frame(width: 110, height: 53)
                    .shadow(radius: 5)
                TextField(inchesPlaceholder, text: Binding<String>(
                    get: { inches.wrappedValue == 0 ? "" : String(inches.wrappedValue) },
                    set: { inches.wrappedValue = Int($0) ?? 0 }
                ), onEditingChanged: { editing in
                    self.isEditingInches = editing
                })
                .frame(width: 90, height: 53) // set a fixed size for the text field
                .padding(.horizontal, 2) // add some padding to the left and right of the text field
                .background(Color.clear)
                .keyboardType(.numberPad)
            }
            .background(lightGreyColor)
            .overlay(
                // Add an orange underline if the TextField is active
                Rectangle()
                    .fill(isEditingInches ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                    .frame(height: 2)
                    .offset(y:27)
                            )
                            .keyboardType(.numberPad)
                        }
                    }
                }







struct DietitianIDCheckField: View {
    @Binding var text: String
    var placeHolder: String
    
    @State private var isEditing = false // track if the TextField is active
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            TextField(placeHolder,text: $text, onEditingChanged: { editing in
                self.isEditing = editing // update isEditing when the TextField is active/inactive
            })
            .autocapitalization(.none)
            .frame(height: 53) // set a fixed height for the text field
            .padding(.horizontal, 10) // add some padding to the left and right of the text field
            //.foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
            .foregroundColor(.black)
            
        }
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.bottom, 5)
        .overlay(
            // Add an orange underline if the TextField is active
            RoundedRectangle(cornerRadius: 10)
                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .offset(y: 26) // adjust the position of the underline
        )
    }
}




struct GenderCheckField: View {
    @Binding var gender: Gender
    
    private var selectedGenderText: (text: String, imageName: String) {
        switch gender {
        case .male:
            return ("Male", "person")
        case .female:
            return ("Female", "person.fill")
        case .unspecified:
            return ("Unspecified", "questionmark.circle" )
        default:
            return ("Select Gender", "person.crop.circle")
        }
    }
    
    var body: some View {
        Text("Select Gender")
        VStack(alignment: .center, spacing: 10) {
            Menu {
                Button(action: { self.gender = .male }) {
                    Label("Male", systemImage: "person")
                }
                
                Button(action: { self.gender = .female }) {
                    Label("Female", systemImage: "person.fill")
                }
                
                Button(action: { self.gender = .unspecified }) {
                    Label("Unspecified", systemImage: "questionmark.circle")
                }
            }
                label: {
                      Label(selectedGenderText.text, systemImage: selectedGenderText.imageName)
                          .foregroundColor(Color(red: 255/255, green: 128/255, blue: 71/255))
                  }
            .menuStyle(BorderlessButtonMenuStyle())
            .padding(.horizontal, 20)
            .padding(.vertical, 10)
            .background(Color.white)
            .cornerRadius(10)
            .shadow(radius: 5)
        }
        .padding(.bottom, 5)
    }
}










//struct WeightCheckField: View {
//    @Binding var value: Double
//    var placeholder: String
//
//    @State private var isEditing = false // track if the TextField is active
//
//    var body: some View {
//        TextField(placeholder, text: Binding<String>(
//            get: { self.value == 0 ? "" : String(format: "%.2f", self.value) },
//            set: {
//                if let newValue = NumberFormatter().number(from: $0)?.doubleValue {
//                    self.value = newValue
//                }
//            }
//        ), onEditingChanged: { editing in
//            self.isEditing = editing
//        })
//        .frame(width: 230, height: 53) // set a fixed size for the text field
//        .padding(.horizontal, 10) // add some padding to the left and right of the text field
//        .background(lightGreyColor)
//        .cornerRadius(5.0)
//        .padding(.bottom, 5)
//        .background(
//            // Add an orange underline if the TextField is active
//            Rectangle()
//                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
//                .frame(height: 2)
//                .offset(y: 26) // adjust the position of the underline
//        )
//        .keyboardType(.decimalPad)
//    }
//}

struct WeightCheckField: View {
    @Binding var value: Double
    var placeholder: String
    
    @State private var isEditing = false // track if the TextField is active
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .shadow(radius: 5)
            
            TextField(placeholder, text: Binding<String>(
                get: { self.value == 0 ? "" : String(format: "%.2f", self.value) },
                set: {
                    if let newValue = NumberFormatter().number(from: $0)?.doubleValue {
                        self.value = newValue
                    }
                }
            ), onEditingChanged: { editing in
                self.isEditing = editing
            })
            .font(.system(size: 17))
            .foregroundColor(.black)
            .padding(.horizontal, 20)
            .frame(height: 53)
            .keyboardType(.decimalPad)
        }
        .frame(height: 53)
        .frame(width: 230) // set a fixed width for the text field and background
        .padding(.horizontal, 30)
        .padding(.bottom, 5)
        .padding(.top, 3)
        .overlay(
            // Add an orange underline if the TextField is active
            Rectangle()
                .fill(isEditing ? Color(red: 255/255, green: 128/255, blue: 71/255) : Color.clear)
                .frame(height: 2)
                .frame(width: 230)
                .offset(y: 26)
        )
    }
    
}














struct SignUpText : View {
    
    var body: some View {
            Image("Image")
            .resizable()
            .frame(width: 220, height: 45)
            .padding(.bottom, 70)
    }
}


struct SignUpButtonContent : View {
    var title: String
    var body: some View {
        return Text(title)
            .font(.headline)
            .foregroundColor(.white)
            .padding()
            .frame(width: 100, height: 40)
            .background(Color(red: 0.892, green: 0.329, blue: 0.082))
            .cornerRadius(200)
            .padding(.bottom, 10)
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}


