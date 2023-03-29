//
//  LoginViewModel.swift
//  messaging
//
//  Created by Howie Nguyen on 1/29/23.
//

import Foundation
import SwiftUI
import shared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesRxSwift
import RxSwift


class LoginViewModel: ObservableObject{
    
    
    @Published var username: String = ""
    @Published var password: String = ""
    @Published  var wrongUsername: Float = 0
    @Published  var wrongPassword: Float  = 0
    @Published  var showingLoginScreen = false
    @Published var loginSuccess = false
    @Published var userId: String = ""
    @Published var dietitianID: String =  UserDefaults.standard.string(forKey: "dietitianID") ?? "Error"
    func login(username: String, password: String) -> Single<String> {
        
        createSingle(for: Api().getGroupIDNative(email: dietitianID))
            .observe(on: MainScheduler.instance)
            .subscribe(onSuccess: { dietitian in
                print("Retrieved dietitian: \(dietitian)")
                self.dietitianID = String(dietitian.intValue)
            }, onFailure: { error in
                print("Error finding dietitian ID: \(error.localizedDescription)")
            })
        return createSingle(for: Api().loginPatientNative(credential: username, password: password))
            .map { response -> String in
                if response == "Log in successful" {
                    
                    DispatchQueue.main.async {
                        self.loginSuccess = true
                        self.userId = username
                        print("dietitianID: \(self.dietitianID )")
                    }
                    
                    return ("Login successful!")
                } else {
                    DispatchQueue.main.async {
                        self.loginSuccess = false
                    }
                    return "Incorrect username or password."
                }
            }
    }





}


