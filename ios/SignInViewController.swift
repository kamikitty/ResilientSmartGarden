//
//  SignInViewController.swift
//  ResilientSmartGarden
//
//  Created by Ruben Marin on 9/19/17.
//  Copyright © 2017 Ruben Marin. All rights reserved.
//

import UIKit

class SignInViewController: UIViewController {

    @IBOutlet weak var usernameTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func signinButton(_ sender: Any) {
        print("sign in button tapped")
    }

    @IBAction func registerButton(_ sender: Any) {
        print("Register account button tapped")
        
        let registerViewController =
            self.storyboard?.instantiateViewController(withIdentifier:
                "RegisterViewController") as!
            RegisterViewController
        
        self.present(registerViewController, animated: true)
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
