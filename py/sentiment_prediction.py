import random

import numpy as np
import torch
import torch.nn as nn
from transformers import BertTokenizer, BertModel


class BERTGRUSentiment(nn.Module):
    def __init__(self,
                 bert,
                 hidden_dim,
                 output_dim,
                 n_layers,
                 bidirectional,
                 dropout):
        super().__init__()
        self.bert = bert
        embedding_dim = bert.config.to_dict()['hidden_size']
        self.rnn = nn.GRU(embedding_dim,
                          hidden_dim,
                          num_layers=n_layers,
                          bidirectional=bidirectional,
                          batch_first=True,
                          dropout=0 if n_layers < 2 else dropout)
        self.out = nn.Linear(hidden_dim * 2 if bidirectional else hidden_dim, output_dim)
        self.dropout = nn.Dropout(dropout)

    def forward(self, text):
        # text = [batch size, sent len]
        with torch.no_grad():
            embedded = self.bert(text)[0]
        # embedded = [batch size, sent len, emb dim]
        _, hidden = self.rnn(embedded)
        # hidden = [n layers * n directions, batch size, emb dim]
        if self.rnn.bidirectional:
            hidden = self.dropout(torch.cat((hidden[-2, :, :], hidden[-1, :, :]), dim=1))
        else:
            hidden = self.dropout(hidden[-1, :, :])
        # hidden = [batch size, hid dim]
        output = self.out(hidden)
        # output = [batch size, out dim]
        return output


def predict_sentiment(sentence):
    model.eval()
    tokens = tokenizer.tokenize(sentence)
    tokens = tokens[:max_input_length - 2]
    indexed = [init_token_idx] + tokenizer.convert_tokens_to_ids(tokens) + [eos_token_idx]
    tensor = torch.LongTensor(indexed).to(device)
    tensor = tensor.unsqueeze(0)
    predictions = model(tensor)
    max_prediction = predictions.argmax(dim=1)
    category_index = max_prediction.item()
    return labels[category_index]


SEED = 1234
random.seed(SEED)
np.random.seed(SEED)
torch.manual_seed(SEED)
torch.backends.cudnn.deterministic = True

PRETRAINED_MODEL_NAME = 'hfl/chinese-bert-wwm-ext'
tokenizer = BertTokenizer.from_pretrained(PRETRAINED_MODEL_NAME)
bert = BertModel.from_pretrained(PRETRAINED_MODEL_NAME)

init_token_idx = tokenizer.cls_token_id
eos_token_idx = tokenizer.sep_token_id
pad_token_idx = tokenizer.pad_token_id
unk_token_idx = tokenizer.unk_token_id
max_input_length = tokenizer.max_model_input_sizes['bert-base-uncased']
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
labels = ['none', 'like', 'disgust', 'happiness', 'sadness', 'anger', 'surprise', 'fear']

HIDDEN_DIM = 256
OUTPUT_DIM = len(labels)
N_LAYERS = 2
BIDIRECTIONAL = True
DROPOUT = 0.25
model = BERTGRUSentiment(bert,
                         HIDDEN_DIM,
                         OUTPUT_DIM,
                         N_LAYERS,
                         BIDIRECTIONAL,
                         DROPOUT)
model.load_state_dict(torch.load('sentiment_prediction_model.pt', map_location=torch.device('cpu')))
