//
//  SharedError.swift
//  messaging
//
//  Created by Howie Nguyen on 1/28/23.
//

import Foundation
import shared

class SharedError: LocalizedError {
    let throwable: KotlinThrowable
    init(_ throwable: KotlinThrowable) {
        self.throwable = throwable
    }
    var errorDescription: String? {
        get { throwable.message }
    }
}
