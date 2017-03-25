//
//  LoginService.swift
//  CSChat
//
//  Created by Hugo Lefrancois on 2017-03-24.
//  Copyright © 2017 Mirego. All rights reserved.
//

import UIKit

protocol LoginService
{
    var loggedUser: User? { get }

    var isLoggedObservable: Observable<Bool> { get }

    func login(username: String, password: String, completion: @escaping (_ user: User?) -> (Void))

    func logout()
}
