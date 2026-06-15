export interface ConversationResponse {
  id: number;
  title: string;
  type: string;
  createdAt: string;
  memberIds: number[];
}

export interface CreateConversationRequest {
  title: string;
  userIds: number[];
}

export interface MessageResponse {
  id: number;
  conversationId: number;
  senderId: number;
  content: string;
  type: string;
  sentAt: string;
}