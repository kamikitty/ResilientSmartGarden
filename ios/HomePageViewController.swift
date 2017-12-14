//
//  HomePageViewController.swift
//  ResilientSmartGarden
//
//  Created by Ruben Marin on 9/19/17.
//  Copyright © 2017 Ruben Marin. All rights reserved.
//

import UIKit

class HomePageViewController: UIViewController {

    @IBOutlet weak var gardenLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func logoutButton(_ sender: Any) {
        print("logged out")
    }

    @IBAction func loadInfoButton(_ sender: Any) {
        print("info loaded")
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
