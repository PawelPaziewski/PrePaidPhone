import React, {Component} from "react";
import {BrowserRouter as Router, Route} from "react-router-dom";

export class BuyPhoneCard extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueName: {},
            valueSurname: {},
            valueAmount: {},
            data: "",
            isResponse: false,
            error : false
        };

        this.getName = this.getName.bind(this);
        this.getSurname = this.getSurname.bind(this);
        this.getAmount = this.getAmount.bind(this);
    }

    private getName(event: any): void {
        this.setState({valueName: event.target.value, isResponse: false, error : false});
    }

    private getSurname(event: any): void {
        this.setState({valueSurname: event.target.value, isResponse: false, error : false});
    }

    private getAmount(event: any): void {
        this.setState({valueAmount: event.target.value, isResponse: false, error : false});
    }

    private putData(): any {
        const url: any = `http://localhost:8080/buyPhoneCard?initialMoney=${this.state.valueAmount}&buyerFirstName=${this.state.valueName}&buyerLastName=${this.state.valueSurname}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        };
        fetch(url, requestOptions)
            .then(response => response.text())
            .then(data => this.setState({data: data, isResponse: true}))
            .catch(
                error => this.setState({error : true})
            );
    }

    render() {
        const error : String = this.state.error ? "error" : " ";
        const hidden : String = !this.state.isResponse ? "hidden" : " ";
        return (<>
                <div className={`modal ${error}`}>
                    <h3>Kupno karty</h3>
                    <div className={`modal-inside`}>
                        <form>
                            <input type="text" placeholder={`Imię`} onChange={this.getName}/>
                            <input type="text" placeholder={`Nazwisko`} onChange={this.getSurname}/>
                            <input type="number" placeholder={`Kwota doładowania`} onChange={this.getAmount} min={1}/>
                            <span className={`hint hidden`}>hint </span>
                            <input type="reset"/>
                        </form>
                        <button onClick={() => this.putData()}>KUP KARTĘ</button>
                    </div>
                </div>
                {this.state.isResponse ?
                <div className={`modal  ${hidden}`}>
                    <div className={`modal-inside sec`}>
                        <div>Imię: <div className={`cardBalance`}>{this.state.valueName}</div></div>
                        <div>Nazwisko: <div className={`cardBalance`}>{this.state.valueSurname}</div></div>
                        <div>Kwota doładowania: <div className={`cardBalance`}>{this.state.valueAmount + " " + "PLN"}</div></div>
                        <div>Wygenerowany numer: { <div className={`cardBalance`}>{this.state.data}</div>}</div>
                    </div>
                </div>
                : <></>}
            </>
        );
    }
}