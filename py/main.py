import flask
import flask_cors

import category_prediction
import sentiment_prediction

app = flask.Flask(__name__)
flask_cors.CORS(app, supports_credentials=True)


@app.route('/get_sentiment', methods=['POST'])
def get_sentiment():
    body = flask.request.get_json()
    text = body['text']
    prediction = sentiment_prediction.predict_sentiment(text)
    res = {'extreme': prediction == 'disgust' or prediction == 'anger'}
    return flask.jsonify(res)


@app.route('/get_category', methods=['POST'])
def get_category():
    body = flask.request.get_json()
    text = body['text']
    category = category_prediction.predict_category(text)
    res = {'category': category}
    return flask.jsonify(res)


@app.route('/get_all_categories', methods=['GET'])
def get_all_categories():
    return flask.jsonify(category_prediction.categories)


if __name__ == '__main__':
    app.run()
