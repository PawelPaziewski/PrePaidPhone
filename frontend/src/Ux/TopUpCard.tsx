import React, {Component} from "react";
import {Route, BrowserRouter as Router} from "react-router-dom";

export class TopUpCard extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueNumber: "",
            valueAmount: "",
            isResponse: false,
            error: false
        };
        this.getPhoneNumber = this.getPhoneNumber.bind(this);
        this.getAmount = this.getAmount.bind(this);
    }

    private getPhoneNumber(event: any): void {
        this.setState({valueNumber: event.target.value,error: false});
    }

    private getAmount(event: any): void {
        this.setState({valueAmount: event.target.value,error: false});
    }

    private topUpCard(): any {

        const url: any = ` http://localhost:8080/topUpCard?phoneNumber=${this.state.valueNumber}&amount=${this.state.valueAmount}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        };
        fetch(url, requestOptions)
            .then(response => { if(response.ok){
                this.setState({isResponse: true});
                return response.text();
            }else{
                this.setState({error : true});
            }
            });
    }
    render() {
        const success : String = this.state.isResponse ? "success" : " ";
        const error : String = this.state.error ? "error" : " ";
        return (<>
                <div className={`modal ${error} ${success}`}>
                    <h3>Doładowanie konta</h3>
                    <div className={`modal-inside`}>
                        <form>
                            <input type="text" placeholder={`Numer telefonu`}
                                   title={`Spróbuj '111 111 111'`} onChange={this.getPhoneNumber}/>
                            <input type="number" placeholder={`Kwota doładowania`}
                                   min={1} onChange={this.getAmount} />
                            <span className={"hint"}>W celu podpowiedzi najedź myszką na pole</span>
                            <input type="reset"/>
                        </form>
                        <button onClick={()=>this.topUpCard()}>DOŁADUJ</button>
                    </div>
                </div>
            </>
        );
    }
}