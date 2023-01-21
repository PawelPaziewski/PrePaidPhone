import React, {Component} from "react";
import {Route, BrowserRouter as Router} from "react-router-dom";

export class SendSms extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueSenderNumber: "",
            valueReceiverNumber: "",
            error: false
        };
        this.getSenderPhoneNumber = this.getSenderPhoneNumber.bind(this);
        this.getReceiverPhoneNumber = this.getReceiverPhoneNumber.bind(this);
    }

    private getSenderPhoneNumber(event: any): void {
        this.setState({valueSenderNumber: event.target.value,});
    }

    private getReceiverPhoneNumber(event: any): void {
        this.setState({valueReceiverNumber: event.target.value,});
    }

    private sendSms(): any {

        const url: any = `http://localhost:8080/sendSms?senderPhoneNumber=${this.state.valueSenderNumber}&receiverPhoneNumber=${this.state.valueReceiverNumber}`;
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
                    <h3>SMS</h3>
                    <div className={`modal-inside`}>
                        <form>
                            <input type="text" placeholder={`Z numeru`} title={"Spróbuj '111 111 111'"}
                                   onChange={this.getSenderPhoneNumber}/>
                            <input type="text" placeholder={`Na numer`} title={"Spróbuj '111 111 112'"}
                                   onChange={this.getReceiverPhoneNumber}/>
                            <textarea placeholder={'Napisz wiadomość... '}/>
                            <span className={"hint"}>W celu podpowiedzi najedź myszką na pole</span>
                            <input type="reset"/>
                        </form>
                        <button onClick={() => this.sendSms()}>WYŚLIJ</button>
                    </div>
                </div>
            </>
        );
    }
}