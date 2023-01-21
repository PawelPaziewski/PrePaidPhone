import React, {Component} from "react";

export class AboutUs extends Component<any, any> {
    constructor(props: any) {
        super(props);

        this.state = {
            isClicked: false
        }

        this.changeState = this.changeState.bind(this);
    }

    private changeState(){
        const currentState : boolean = this.state.isClicked;
        this.setState({isClicked: !currentState})
    }
    render() {
        return (<>
                <div className={`modal`}>
                    <h3>Twórcy projektu</h3>
                    <div className={`modal-inside`}>
                        <h2>Paweł Paziewski-Kopczewski</h2>
                        <h2>Filip Pawłowicz</h2>
                        <button onClick={this.changeState}>DZIĘKUJEMY</button>
                    </div>
                </div>
                {this.state.isClicked ?
                    <div className={`modal`}>
                        <div className={`modal-inside sec`}>
                            <img src={`https://mateusz9555.files.wordpress.com/2010/06/panda31.jpg`}/>
                        </div>
                    </div>
                    : <></>
                }
            </>
        );
    }
}