//
//  SignUpViewModel.swift
//  messaging
//
//  Created by Howie Nguyen on 2/22/23.
//

import Foundation
import SwiftUI
import shared
import KMPNativeCoroutinesCombine
import Combine
import KMPNativeCoroutinesRxSwift
import RxSwift
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesAsync


//import KMPNativeCoroutinesCore
//import KMPNativeCoroutinesRxSwift
//import KMPNativeCoroutinesCombine
//import KMPNativeCoroutinesAsync
class SignUpViewModel: ObservableObject{
    @Published var userID: String = ""
    @Published var password: String = ""
    @Published var email: String = ""
    @Published var fullname: String = ""
    @Published var dietitianID: String = ""
    @Published var age: Int = 0
    @Published var height: Int = 0
    @Published var weight: Double = 0
    @Published var gender = Gender.unspecified
    let api = Api()
    
    private let disposeBag = DisposeBag()
    
    
    
    
    
    
    
    private var cancellables = Set<AnyCancellable>()
    @Published var groupId: Int?
    
    
    // TODO: remove Never as Future's failure type; just using that here to get the code to build faster
    func register(fullName: String, userId: String, password: String, email: String, dietitianID: String, age: Int, height: Int, weight: Double, gender: Gender)  {
        
        //edge cases for login verification:
            
        createSingle(for: self.api.getGroupIDNative(email: dietitianID))
            .observe(on: MainScheduler.instance)
            .subscribe(onSuccess: { groupID in
                print("Retrieved Dietician: \(groupID)")
                
                self.dietitianID = String(groupID.intValue)
                


                //let chatViewModel = ChatViewModel(dietitianID: String(groupID.intValue), patientID: userId)
                let patientObject = Patient(fullName: fullName, userId: userId, password: password, email: email, groupId: groupID, pfp: "something", role: UserRole.patient, lastSentAt: TimestampKt.currentMoment(), age: KotlinInt(integerLiteral: self.age), height: KotlinInt(integerLiteral: self.height), weight: KotlinDouble(value: self.weight), bloodPressure: "", allergens: "", gender: self.gender, lastVisit: "", currentPrescriptions: "", notes: "")

                print("---- \(groupID)")
                

                createSingle(for: self.api.addChatRoomNative(patient: patientObject))
                                    .flatMap { response -> Single<String> in
                                        if response == "Successfully added chatRoom" {
                                            return createSingle(for: self.api.registerPatientNative(patientObject: patientObject))
                                        } else {
                                            return Single.error(MyError.someError)
                                        }
                                    }
                                    .subscribe(
                                        onSuccess: { response in
                                            print("Successfully registered patient and added chat room")
                                            UserDefaults.standard.set(String(dietitianID), forKey: "dietitianID")

                                        },
                                        onFailure: { error in
                                            print("Received error: \(error)")
                                        }
                                    )
                                    .disposed(by: self.disposeBag)

            }, onFailure: { error in
                print("Received error: \(error)")
            })
            .disposed(by: disposeBag)






        
        
        
        
    }
}

enum MyError: Error {
    case someError
}


    






