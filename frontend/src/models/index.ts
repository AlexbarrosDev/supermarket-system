export interface GroupDetailsDTO {
  id: number;
  name: string;
}

export interface GroupCreateDTO {
  name: string;
}

export interface GroupUpdateDTO {
  name: string;
}

export interface CategoryDetailsDTO {
  id: number;
  name: string;
}

export interface CategoryCreateDTO {
  name: string;
}

export interface CategoryUpdateDTO {
  name: string;
}

export interface ProductSummaryDTO {
  id: number;
  name: string;
}

export interface ProductDetailsDTO {
  id: number;
  name: string;
  currentPrice: number;
  category: CategoryDetailsDTO;
  group: GroupDetailsDTO;
  status: ProductStatus;
  createdAt: string;
}

export interface ProductCreateDTO {
  name: string;
  currentPrice: number;
  categoryId: number;
  groupId: number;
  status: ProductStatus;
}

export interface ProductUpdateDTO {
  name: string;
  currentPrice: number;
  categoryId: number;
  groupId: number;
  status: ProductStatus;
}

export type ProductStatus = 'ACTIVE' | 'INACTIVE';

export interface ClientSummaryDTO {
  id: number;
  name: string;
}

export interface ClientDetailsDTO {
  id: number;
  name: string;
  cpf: string;
  status: ClientStatus;
  phone: string;
  registrationDate: string;
  address: AddressDetailsDTO;
}

export interface ClientCreateDTO {
  name: string;
  cpf: string;
  status: ClientStatus;
  phone?: string;
  address: AddressCreateDTO;
}

export interface ClientUpdateDTO {
  name?: string;
  phone?: string;
  address?: AddressUpdateDTO;
}

export interface ClientStatusUpdateDTO {
  status: ClientStatus;
}

export type ClientStatus = 'ACTIVE' | 'INACTIVE' | 'BLOCKED';

export interface AddressDetailsDTO {
  id: number;
  street: string;
  number: string;
  city: string;
  state: string;
  zip: string;
}

export interface AddressCreateDTO {
  street: string;
  number: string;
  city: string;
  state: string;
  zip: string;
}

export interface AddressUpdateDTO {
  street?: string;
  number?: string;
  city?: string;
  state?: string;
  zip?: string;
}

export interface SaleSummaryDTO {
  id: number;
  saleDate: string;
  status: SaleStatus;
  client: ClientSummaryDTO;
  total: number;
}

export interface SaleDetailsDTO {
  id: number;
  saleDate: string;
  status: SaleStatus;
  client: ClientSummaryDTO;
  items: ItemSoldDetailsDTO[];
  total: number;
}

export interface SaleCreateDTO {
  clientId: number;
  items: ItemSoldCreateDTO[];
}

export interface SaleStatusUpdateDTO {
  status: SaleStatus;
}

export type SaleStatus = 'FINALIZED' | 'CANCELED';

export interface ItemSoldDetailsDTO {
  product: ProductSummaryDTO;
  quantity: number;
  unitPrice: number;
  subTotal: number;
}

export interface ItemSoldCreateDTO {
  productId: number;
  quantity: number;
}
