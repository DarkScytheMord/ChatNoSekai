export interface BotResponse {
  id: number;
  userId: number;
  question: string;
  answer: string;
  createdAt: string;
}

export interface BotHistoryResponse {
  id: number;
  question: string;
  answer: string;
  createdAt: string;
}