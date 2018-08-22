import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase)

export const onTokenCreateBySupervisor = functions.database
.ref('/SupervisorTrigger/{pushID}')
.onCreate((snapshot, context) => {
    const data = snapshot.val()
    const boolean_token = data.boolean_token
    const location = data.location
    const messageId = data.messageId

    const sensor_Id = data.sensor_id
    console.log(`New token with Boolean: ${boolean_token} from sensor: ${sensor_Id}`);

    
    
    var payload = {
        notification: {
            title: "New Job Assigned",
            body: location +"",
            sound: 'default',
        }
    };

    admin.messaging().sendToDevice(messageId, payload)
        .then(function (response) {
            console.log("Successfully sent message:", response);
        })
        .catch(function (error) {
            console.log("Error sending message:", error);
    });
    return Promise.resolve(data)
});

export const onTokenCreateBySensor = functions.database
.ref('/FloorsTokens/{pushID}')
.onCreate((snapshot, context) => {
    const data = snapshot.val()
    const boolean_token = data.boolean_token
    const sensor_Id = data.sensor_id
    console.log(`New token with Boolean: ${boolean_token} from sensor: ${sensor_Id}`);

    var clientMessageToken = [
        "cFPKYO3QBR0:APA91bGPyAOgTrCpC086PIXwdR01u2qdRSXp_5dKrfseMNceFL8SE-GYj9rO2ppoKzuyEdpX69CAxGwVDmx2Y3CNl1BgkaEekCPhQ9PHqgAgxr_LMkUdGVqztIvd-5b53eh37xF5ANkgg8CCej6VtMgKDvSfR-mB_g",
        "dThEUqC4hQA:APA91bG369yt-wruKNxMkM2VKr62qPdoXmWpMBLj2BkNYYqW21xTixLQqSmaNSybxGJc3oH7YYWRvAh2UwQcbDu-WOQ0bU77fIqZuhK4aNMiTT7P_g3YJiTiOhr3lYGBnbox0Ed-z2FJS4uOBHYDXBuEqOfwgBwbKA"
    ];
    
    
    var payload = {
        notification: {
            title: "New Job Assigned",
            body: sensor_Id +"",
            sound: 'default',
        }
    };

    admin.messaging().sendToDevice(clientMessageToken, payload)
        .then(function (response) {
            console.log("Successfully sent message:", response);
        })
        .catch(function (error) {
            console.log("Error sending message:", error);
    });
    return Promise.resolve(data)
});

export const onTokenUpdateBySensor = functions.database
.ref('/FloorsTokens/{pushID}')
.onUpdate((change , context) => {
    const data = change.after.val()
    const boolean_token = data.boolean_token

    if(boolean_token == 0){
        return change.after.ref.remove()
    }
    
    return null
});

export const onTokenUpdateBySupervisor = functions.database
.ref('/SupervisorTrigger/{pushID}')
.onUpdate((change , context) => {
    const data = change.after.val()
    const boolean_token = data.boolean_token

    if(boolean_token == 0){
        return change.after.ref.remove()
    }
    
    return null
});