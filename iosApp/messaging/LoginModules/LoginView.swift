//
//  LoginView.swift
//  messaging
//
//  Created by Howie Nguyen on 1/24/23.
//

import Foundation
import SwiftUI
import shared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesRxSwift
import RxSwift

let lightGreyColor = Color(red: 239.0/255.0, green: 243.0/255.0, blue: 244.0/255.0, opacity: 1.0)
let darkOrange = Color(red: 227/255.0, green: 84/255.0, blue: 21/255.0, opacity: 1.0)

struct LoginView: View {
    
    @ObservedObject private var vm = LoginViewModel()
    @ObservedObject private var vm2 = SignUpViewModel()
    
    
    @State private var username = ""
    @State private var password = ""
    @State private var showAlert = false
    @State private var alertMessage = ""
    private let disposeBag = DisposeBag()
    
    var body: some View {
        
        NavigationView{
        
                VStack() {
                    WelcomeText()
                   
                    
                    PlainTextField(text: $username, placeHolder: "User Name")
                        .border(.red, width: CGFloat(vm.wrongUsername))
                    
                    PasswordField(text: $password, placeHolder: "Password")
                        .border(.red, width: CGFloat(vm.wrongPassword))
                    
                    
                    Spacer().frame(height: 22)
                    Button(action: {
                         vm.username = username
                         vm.password = password
                        
                       
                        vm.login(username: username, password: password)
                        .observe(on: MainScheduler.instance)
                        .subscribe(onSuccess: { message in
                                                    if vm.loginSuccess {
                                                        DispatchQueue.main.async {
                                                            vm.showingLoginScreen = true
                                                        }
                                                        showAlert = true
                                                        alertMessage = message
                                                        vm.wrongPassword = 0
                                                        vm.wrongUsername = 0
                                                    
                                                       
                                                    } else {
                                                        vm.showingLoginScreen = false
                                                        showAlert = true
                                                        alertMessage = message
                                                        vm.wrongPassword = 2
                                                        vm.wrongUsername = 2
                                              
                                                    }
                                                })
                                                .disposed(by: disposeBag)
                            

                           
                           
                        
                    })
                    {
                        LoginButtonContent(title: "Login")
                    }
                   
                    Spacer().frame(height:13)
               
                    NavigationLink(destination: ChatView(chatViewModel: ChatViewModel(dietitianID: vm.dietitianID, patientID: vm.username)), isActive: $vm.showingLoginScreen) {
                        EmptyView()
                    }.hidden()
                    
                    

                    
                    
                  // Spacer(minLength: 5) // add some vertical spacing
                    
                    NavigationLink(destination: SignUpView()){
                        LoginButtonContent(title: "Sign Up")
                    }
                    

                    
                    
                    
                    
                }.padding([.leading, .trailing], 27.5)
                
    }.navigationBarHidden(true)
    
  }
  
    
}



struct PlainTextField: View{
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
        .frame(height: 53)
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

struct PasswordField: View{
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
        .frame(height: 53)
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











struct WelcomeText : View {
    
    var body: some View {
       
            
        Image("Image")
            .resizable()
            .frame(width: 220, height: 45)
            .padding(.bottom, 70)
            
    }
}


struct LoginButtonContent : View {
    var title: String
    var body: some View {
        return Text(title)
            .font(.headline)
            .foregroundColor(.white)
            .padding()
            .frame(width: 100, height: 40)
            .background(Color(red: 0.892, green: 0.329, blue: 0.082))
            .cornerRadius(200)
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}


