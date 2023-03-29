

//
//  ChatViewModel.swift
//  messaging
//
//  Created by Howie Nguyen on 2/28/23.
//
import Foundation
import SwiftUI
import KMPNativeCoroutinesCombine
import Combine
import shared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesRxSwift
import RxSwift
import KMPNativeCoroutinesAsync
import JavaScriptCore


//@ObservedObject var user =  LoginViewModel()
// ChatViewModel that manages the chat messages


class ChatViewModel: ObservableObject {
    
    let patientID: String
    var dietitianID: String = ""
    var chatRoomId: Int32 = 0
    @Published var chatRoom: ChatRoom?
    @Published var messages: [Message] = []
    @Published var messageText: String = ""
   
    
    let api = Api()
    //let chatRoomId = 79131306
    
    private var messageCancellable = Set<AnyCancellable>()
    private var chatRoomCancellable = Set<AnyCancellable>()
    private var addChatRoom = Set<AnyCancellable>()
    
    
    private var disposeBag = DisposeBag()
    
    init(dietitianID: String, patientID: String){
        
        self.dietitianID = dietitianID
        self.patientID = patientID
        
        
        //self.chatRoom =  ChatRoom(chatRoomId:Int32(chatRoomId), dietitianId: dietitianID, patientId: patientID)
        
        
        
        
        if(!ApiKt.socketClient._job.isActive){
            print("Starting WebSocket connection...")
            ApiKt.socketClient._job.start()
        }
        
        
        createSingle(for: api.getDietitianNative(groupId: Int32(dietitianID) ?? 0))
            .observe(on: MainScheduler.instance)
            .subscribe(onSuccess: { dietitian in
                print("Retrieved dietitian: \(dietitian)")
                
                
                // Use the dietitianId to find the chat room ID
                createSingle(for: self.api.findChatRoomIdNative(dietitianId: dietitian, patientId: self.patientID))
                    .observe(on: MainScheduler.instance)
                    .subscribe(onSuccess: { roomId in
                        print("Retrieved chat room ID: \(roomId)")
//                        self.chatRoomId = Int32(truncating: roomId)
                        
                        // Use the chat room ID to initialize the chat room and get messages
                        self.chatRoom = ChatRoom(chatRoomId: Int32(roomId), dietitianId: self.dietitianID, patientId: self.patientID)
        
                            //self.getMessages()
                        
                        self.getMessages()
                        
                            self.receiveMessages()
                        
                    }, onFailure: { error in
                        print("Error finding chat room ID: \(error.localizedDescription)")
                    })
            }, onFailure: { error in
                print("Error finding dietitian ID: \(error.localizedDescription)")
            })

        
        
        
    
        
        
        
        
    }
    
    
    
    func sendMessage(){
        guard !messageText.isEmpty else { return }
        let message = Message(
            chatRoomId: chatRoom?.chatRoomId ?? 0,
            messageText: messageText,
            sentTime: TimestampKt.currentMoment(),
            senderID: patientID,
            isRead: false
            
        )
        
        
        let publisher = createPublisher(for: api.addMessageNative(message: message))
            .map { _ in message }
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in
                if case .failure(let error) = completion {
                    print("Failed to send message: \(error.localizedDescription)")
                }
            }, receiveValue: { message in
                self.messages.append(message)
            })
        
        
        messageCancellable.insert(publisher)
        
        ApiKt.socketClient.send(message: message.messageText)
        messageText = ""
        
        
    }
    
    
    func getMessages() {
//        messages.removeAll()
        createPublisher(for: api.getMessageNative(chatroomId: chatRoom?.chatRoomId ?? 0))
            .map { messages -> [Message] in
                messages.map { message2 in
                    Message(
                        chatRoomId: message2.chatRoomId,
                        messageText: message2.messageText,
                        sentTime: message2.sentTime,
                        senderID: message2.senderID,
                        isRead: message2.isRead
                    )
                }
            }
            .receive(on: DispatchQueue.main) // receive on the main
            .sink(
                receiveCompletion: { completion in
                    // handle completion, if needed
                    print("\(completion)")
                },
                receiveValue: { [weak self] messages in
                    guard let self = self else { return }
                    let test=self.getMessages()
                    self.messages = messages
                    self.objectWillChange.send()
                }
            )
            .store(in: &messageCancellable)
    }
    




    
    

    
    
     func receiveMessages() {
        createPublisher(for: ApiKt.socketClient.chatMessageNative)
            .map { receivedMessage -> Message in
                return Message(
                    chatRoomId: 0,
                    messageText: receivedMessage,
                    sentTime: TimestampKt.currentMoment(),
                    senderID: self.dietitianID,
                    isRead: false
                )
            }
            .receive(on: DispatchQueue.main) // receive on the main
            .sink(
                receiveCompletion: { completion in
                    // handle completion, if needed
                    print("\(completion)")
                },
                receiveValue: { [weak self] message in
                    guard let self = self else { return }
                    self.messages.append(message)
                    self.messages.removeLast()
//                    self.messages = messages
                    self.objectWillChange.send()
                }
            )
            .store(in: &messageCancellable)
    }


    var shouldShowDateMessage: Bool {
        guard let latestMessage = messages.last else { return false }
        return !TimestampKt.isInstantSameDay(instant: latestMessage.sentTime!)
    }

    var dateMessageText: String {
        return TimestampKt.displayDate(momentText: TimestampKt.currentMoment())
    }

    
    
    




   

    

    






 






    



    
    
    

    
   
}










