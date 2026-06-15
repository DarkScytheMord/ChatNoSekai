export type LibraryItemType = 'ANIME' | 'MANGA';

export type LibraryItemStatus =
  | 'WATCHING'
  | 'COMPLETED'
  | 'PENDING'
  | 'DROPPED'
  | 'FAVORITE';

export interface LibraryItemResponse {
  id: number;
  userId: number;
  title: string;
  type: LibraryItemType;
  status: LibraryItemStatus;
  description: string;
  imageUrl: string;
  rating: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateLibraryItemRequest {
  title: string;
  type: LibraryItemType;
  status: LibraryItemStatus;
  description: string;
  imageUrl: string;
  rating: number;
}

export interface UpdateLibraryItemRequest {
  title: string;
  type: LibraryItemType;
  status: LibraryItemStatus;
  description: string;
  imageUrl: string;
  rating: number;
}