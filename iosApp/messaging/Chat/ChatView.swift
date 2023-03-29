//
//  ChatView.swift
//  messaging
//
//  Created by Howie Nguyen on 3/2/23.
//


import Foundation
import SwiftUI
import shared
import KMPNativeCoroutinesRxSwift
import RxSwift
import KMPNativeCoroutinesCombine
import Combine

struct ChatView: View {
    
    @ObservedObject private var viewModel: ChatViewModel
    private var cancellables = Set<AnyCancellable>()
    private let api = Api()
    @State private var textFieldHeight: CGFloat = 51
    private var prevMessageDate: String = ""
    
    
    private let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "MMM d, yyyy"
        return formatter
    }()
    
    private var lastDisplayedDate: Date?
    
    
    
    init(chatViewModel: ChatViewModel){
        self.viewModel = chatViewModel
    }
    
    private func textEditorHeight(text: String) -> CGFloat {
        let size = CGSize(width: 314, height: 800)
        let estimatedSize = text.boundingRect(with: size, options: [.usesLineFragmentOrigin], attributes: [.font: UIFont.systemFont(ofSize: 16)], context: nil)
        return estimatedSize.height
    }
    
    var body: some View {
        VStack {
            
            ScrollViewReader { proxy in
                ScrollView {
                    VStack(spacing: 10) {
                        // Group messages by date
                        let messagesByDate = Dictionary(grouping: viewModel.messages, by: { TimestampKt.displayDate(momentText: $0.sentTime!) })
                        
                        // Display messages for each date
                        ForEach(messagesByDate.sorted(by: { $0.key > $1.key }), id: \.0) { (dateString, messages) in
                            Text(dateString)
                                .foregroundColor(.black)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 5)
                                .background(Color(red: 0.883, green: 0.833, blue: 0.883, opacity: 0.12))
                                .cornerRadius(16)
                                .padding(.top, 20)
                                .padding(.horizontal)
                            
                            // Display messages for this date
                            ForEach(messages, id: \.messageId) { message in
                                ChatBox(message: message, userID: viewModel.patientID)
                            }
                        }
                    }
                    .padding(.horizontal)
                    .onAppear {
                        viewModel.getMessages()
                    }
                    .onChange(of: viewModel.messages) { messages in
                        if !messages.isEmpty {
                            let lastMessageID = messages[messages.count-1].messageId
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                                withAnimation {
                                    proxy.scrollTo(lastMessageID, anchor: .bottom)
                                }
                            }
                        }
                    }
                }
            }

            
            ZStack {
                RoundedRectangle(cornerRadius: 25)
                    .fill(Color(.systemGray6))
                    .frame(height: 50)
                
                HStack {
                    TextField("send message", text: $viewModel.messageText)
                        .padding(.horizontal)
                    
                    
                    if viewModel.messageText.isEmpty {
                        Image(systemName: "arrow.up.circle.fill")
                            .foregroundColor(Color(.systemGray2))
                            .imageScale(.large)
                            .padding(.trailing)
                    } else {
                        Button(action: viewModel.sendMessage) {
                            Image(systemName: "arrow.up.circle.fill")
                                //.foregroundColor(.orange)
                                .foregroundColor(Color(red: 1, green: 0.502, blue: 0.279, opacity: 1))
                                .imageScale(.large)
                                .padding(.trailing)
                        }
                    }
                }
                .frame(height: 50)
                
            }
            .padding()
            
        }
        
        .toolbar {
            ToolbarItem(placement: .principal) {
//                Image(systemName: "person.crop.circle.fill")
                Image("default_icon")
                    .resizable()
                    .font(.title)
                    
                    .frame(width: 30 , height: 30)
                    .cornerRadius(20)
                    //.foregroundColor(.blue)
            }
        }
        .toolbarBackground(
                // Color(red: 0.883, green: 0.833, blue: 0.883, opacity: 0.12)
                Color(red: 1, green: 0.933, blue: 0.902, opacity: 1),
                for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
        }
    
}



    
    
    
    struct ChatBox: View {
        
        let message: Message
                let userID : String
                
                
                private let formatter: DateFormatter = {
                    let formatter = DateFormatter()
                    formatter.dateFormat = "h:mm a"
                    return formatter
                }()
                
                private let dateFormatter: DateFormatter = {
                    let formatter = DateFormatter()
                    formatter.dateFormat = "MMM d"
                    return formatter
                }()
                
                
                var body: some View {
                    HStack(alignment: .top) {
                        if message.senderID != userID {
                            Spacer().frame(width:10)
                            Image("default_icon")
                                .resizable()
                                .frame(width: 42, height: 42)
                                .cornerRadius(20)
                                .padding(.trailing, 8)
                            VStack(alignment: .leading, spacing: 4) {
                                Text(message.messageText)
                                    .foregroundColor(.black)
                                    .padding(10)
                             
                                    .background(
                                        RoundedRectangle(cornerRadius: 16)
                                            .fill(Color(red: 0.914, green: 0.91, blue: 0.91, opacity: 1))
                                            
                                            
                                    )
                                
                                
                                
                                    .fixedSize(horizontal: false, vertical: true) // set a fixed height for horizontal expansion only
                                    //.frame(maxWidth: .infinity, alignment: .leading)
                                    .frame(maxWidth: 250, alignment: .leading) // set max width to 250 and align the text to the leading edge
                                    .padding(.leading, 4)
                                    .padding(.trailing, 10)
                                    .padding(.top, 5)
                                   // .padding(.bottom, 10)
                                
                                //Spacer()
                                HStack {
                                    //Spacer()
                                    if(TimestampKt.isInstantSameDay(instant: message.sentTime!)){
                                        Text(formatter.string(from: Date(timeIntervalSince1970: Double(message.sentTime!.epochSeconds))))
                                            .foregroundColor(.gray)
                                            .font(.footnote)
                                            
                                    } else {
                                        Text(dateFormatter.string(from: Date(timeIntervalSince1970: Double(message.sentTime!.epochSeconds))))
                                            .foregroundColor(.gray)
                                            .font(.footnote)
                                          
                                    }
                                    Spacer()
                                }
                                .padding(.trailing, 15)
                            }
                 
                    
                    //Spacer()
                } else {
                    Spacer()
                    VStack(alignment: .trailing, spacing: 4) {
                        Text(message.messageText)
                            .foregroundColor(.white)
                            .padding(10)
                            .background(
                                RoundedRectangle(cornerRadius: 16)
                                    //.fill(Color(red: 255/255, green: 213/255, blue: 128/255, opacity: 1))
                                    //.frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topTrailing)
                                    .fill(Color(red: 1, green: 0.502, blue: 0.279, opacity: 1))
                            )
                            .fixedSize(horizontal: false, vertical: true) // set a fixed height for horizontal expansion only
                            .frame(maxWidth: 250, alignment: .trailing)
                            .padding(.leading, 20)
                            .padding(.trailing, 4)
                            .padding(.top, 5)
                           //.padding(.bottom, 10)
                        
                        HStack{
                            Spacer()
                            if(TimestampKt.isInstantSameDay(instant: message.sentTime!)){
                                Text(formatter.string(from: Date(timeIntervalSince1970: Double(message.sentTime!.epochSeconds))))
                                    .foregroundColor(.gray)
                                    .font(.footnote)
                                
                            } else {
                                Text(dateFormatter.string(from: Date(timeIntervalSince1970: Double(message.sentTime!.epochSeconds))))
                                    .foregroundColor(.gray)
                                    .font(.footnote)
                                
                                
                            }
                            
                          
                        }
                        
                        .padding(.trailing, 8)
                        
                        
                        
                    }
                  
                }
                
            }
        }
        
    }

    

       


