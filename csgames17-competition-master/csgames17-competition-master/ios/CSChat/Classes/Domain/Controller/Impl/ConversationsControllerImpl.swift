//
//  ConversationsControllerImpl.swift
//  CSChat
//
//  Created by Hugo Lefrancois on 2017-03-24.
//  Copyright © 2017 Mirego. All rights reserved.
//

import UIKit

class ConversationsControllerImpl: ConversationsController
{
    private let loginService: LoginService
    private let conversationsService: ConversationsService
    private let dateFormatter: MessageDateFormatter

    init(loginService: LoginService, conversationsService: ConversationsService, dateFormatter: MessageDateFormatter)
    {
        self.loginService = loginService
        self.conversationsService = conversationsService
        self.dateFormatter = dateFormatter
    }

    func allConversations(completion: @escaping (_ conversations: [ConversationViewModel]?) -> (Void))
    {
        guard let loggedUser = loginService.loggedUser else {
            completion(nil)
            return
        }

        conversationsService.allConversations(loggedUser: loggedUser) { (conversationResponse) -> (Void) in
            guard let conversationResponse = conversationResponse, let users = conversationResponse.users else {
                completion(nil)
                return
            }

            var conversationsViewModel: [ConversationViewModel] = []
            if let conversations = conversationResponse.conversations {
                conversations.forEach{
                    conversationsViewModel.append(ConversationViewModelImpl(conversationSummary: $0, users: users, loggedUser: loggedUser, dateFormatter: self.dateFormatter))
                }
            }
            completion(conversationsViewModel)
        }
    }

    func allMessages(forConversation conversation: ConversationViewModel, completion: @escaping (_ messages: [MessageViewModel]?) -> (Void))
    {
        guard let loggedUser = loginService.loggedUser, let conversationImpl = conversation as? ConversationViewModelImpl else {
            completion(nil)
            return
        }

        conversationsService.allMessages(forConversationSummary: conversationImpl.conversationSummary, loggedUser: loggedUser) { (messageResponse) -> (Void) in
            if let messageResponse = messageResponse, let conversation = messageResponse.conversations?.first, let users = messageResponse.users {
                var messageViewDatas: [MessageViewModel] = []
                if let messages = conversation.messages {
                    messages.forEach {
                        messageViewDatas.append(MessageViewModelImpl(message: $0, users: users, dateFormatter: self.dateFormatter))
                    }
                }
                completion(messageViewDatas)
            } else {
                completion(nil)
            }
        }
    }

    func sendMessage(message: String, conversationViewModel: ConversationViewModel, completion: @escaping (_ messages: [MessageViewModel]?) -> (Void))
    {
        guard let loggedUser = loginService.loggedUser, let conversationImpl = conversationViewModel as? ConversationViewModelImpl else {
            completion(nil)
            return
        }

        conversationsService.sendMessage(message: message, conversationSummary: conversationImpl.conversationSummary, loggedUser: loggedUser) { (messageResponse) -> (Void) in
            if let messageResponse = messageResponse, let conversation = messageResponse.conversations?.first, let users = messageResponse.users {
                var messageViewDatas: [MessageViewModel] = []
                if let messages = conversation.messages {
                    messages.forEach {
                        messageViewDatas.append(MessageViewModelImpl(message: $0, users: users, dateFormatter: self.dateFormatter))
                    }
                }
                completion(messageViewDatas)
            } else {
                completion(nil)
            }
        }
    }
}
