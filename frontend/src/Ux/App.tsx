import React, {Component, ReactNode} from 'react';
import {BuyPhoneCard} from "./BuyPhoneCard";
import {CardBalance} from "./CardBalance";
import {TopUpCard} from "./TopUpCard";
import {SendSms} from "./SendSms";
import {MakePhoneCall} from "./MakePhoneCall";
import {Billing} from "./Billing";
import {BrowserRouter as Router, Link, Route, Routes} from "react-router-dom";
import {AboutUs} from "./AboutUs";
import {StartPage} from "./StartPage";

import '../Styles/App.css';
import '../Styles/NavBar.css';
import "../Styles/TopBar.css";

export class App extends Component<any, any> {
    render(): ReactNode {
        const logo = require('./pre-paid-phone.png');
        return (<>
                <Router>
                    <div className={`topBar`}>
                        <div><Link className={"link"} to={"/"}><img src={logo} /></Link></div>
                        <div><Link className={"link"} to={"/aboutUs"}>O Nas</Link></div>
                    </div>
                    <div className={`navBar`}>
                        <li><Link className={"link"} to={"/buyPhoneCard"}>Kupno Karty</Link></li>
                        <li><Link className={"link"} to={"/makePhoneCall"}>Połączenie</Link></li>
                        <li><Link className={"link"} to={"/sendSms"}>SMS</Link></li>
                        <li><Link className={"link"} to={"/cardBalance"}>Saldo Konta</Link></li>
                        <li><Link className={"link"} to={"/topUpCard"}>Doładowanie konta</Link></li>
                        <li><Link className={"link"} to={"/billing"}>Billing</Link></li>
                    </div>
                    <div className="App">
                        <div className="App-container">
                            <Routes>
                                <Route path={"/"} element={<StartPage/>}/>
                                <Route path={"/buyPhoneCard"} element={<BuyPhoneCard/>}/>
                                <Route path={"/cardBalance"} element={<CardBalance/>}/>
                                <Route path={"/makePhoneCall"} element={<MakePhoneCall/>}/>
                                <Route path={"/topUpCard"} element={<TopUpCard/>}/>
                                <Route path={"/sendSms"} element={<SendSms/>}/>
                                <Route path={"/billing"} element={<Billing/>}/>
                                <Route path={"/aboutUs"} element={<AboutUs/>}/>
                            </Routes>
                        </div>
                    </div>
                </Router>
            </>
        );
    }

}

