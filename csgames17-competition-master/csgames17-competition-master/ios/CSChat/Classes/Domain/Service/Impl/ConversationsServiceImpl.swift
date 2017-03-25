//
//  ConversationsServiceImpl.swift
//  CSChat
//
//  Created by Hugo Lefrancois on 2017-03-24.
//  Copyright © 2017 Mirego. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireObjectMapper

class ConversationsServiceImpl: ConversationsService
{
    func allConversations(loggedUser: User, completion: @escaping (_ conversationResponse: ConversationResponse?) -> (Void))
    {
        let url = HttpConst.baseUrl + "users/\(loggedUser.id ?? "")/conversations"

        Alamofire.request(url, method: .get, encoding: JSONEncoding.default).responseObject { (response: DataResponse<ConversationResponse>) in
            if let statusCode = response.response?.statusCode {
                if (statusCode >= 200 && statusCode < 300) {
                    completion(response.result.value)
                } else {
                    completion(nil)
                }
            } else {
                completion(nil)
            }
        }
    }

    func allMessages(forConversationSummary conversationSummary: ConversationSummary, loggedUser: User, completion: @escaping (_ messageResponse: MessageResponse?) -> (Void))
    {
        let url = HttpConst.baseUrl + "users/\(loggedUser.id ?? "")/conversations/\(conversationSummary.id ?? "")"
        Alamofire.request(url, method: .get, encoding: JSONEncoding.default).responseObject { (response: DataResponse<MessageResponse>) in
            if let statusCode = response.response?.statusCode {
                if (statusCode >= 200 && statusCode < 300) {
                    completion(response.result.value)
                } else {
                    completion(nil)
                }
            } else {
                completion(nil)
            }
        }
    }

    func sendMessage(message: String, conversationSummary: ConversationSummary, loggedUser: User, completion: @escaping (_ messageResponse: MessageResponse?) -> (Void))
    {
        let url = HttpConst.baseUrl + "users/\(loggedUser.id ?? "")/conversations/\(conversationSummary.id ?? "")"
        let parameters: [String: Any] = ["message": message]
        Alamofire.request(url, method: .post, parameters: parameters, encoding: JSONEncoding.default).responseObject { (response: DataResponse<MessageResponse>) in
            if let statusCode = response.response?.statusCode {
                if (statusCode >= 200 && statusCode < 300) {
                    completion(response.result.value)
                } else {
                    completion(nil)
                }
            } else {
                completion(nil)
            }
        }
    }
}
