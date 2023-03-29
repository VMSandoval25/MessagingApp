//
//  ForgotPassword.swift
//  MVISample
//
//  Created by Mehsam Saeed on 08/11/2022.
//
import SwiftUI

struct ForgotPasswordView: View {
    @Environment(\.presentationMode) var presentationMode
    var body: some View {
        
        ZStack{
            Color.white
            VStack{
                HStack{
                    Spacer()
                        .frame(width:10)
                    Button("Dismiss Me") {
                                presentationMode.wrappedValue.dismiss()
                            }
                    
                    Spacer()
                }
               
                Spacer()
                Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
                Spacer()
            }
        }
    }
}

struct ForgotPassword_Previews: PreviewProvider {
    static var previews: some View {
        ForgotPasswordView()
    }
}
