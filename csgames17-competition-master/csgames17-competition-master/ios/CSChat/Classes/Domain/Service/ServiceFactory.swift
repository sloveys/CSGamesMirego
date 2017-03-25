//
//  ServiceFactory.swift
//  CSChat
//
//  Created by Hugo Lefrancois on 2017-03-24.
//  Copyright © 2017 Mirego. All rights reserved.
//

import UIKit

class ServiceFactory
{
    let uniqueloginService = LoginServiceImpl()
    let uniqueConversationsService = ConversationsServiceImpl()

    func loginService() -> LoginService
    {
        return uniqueloginService
    }

    func conversationsService() -> ConversationsService
    {
        return uniqueConversationsService
    }
}
