import { api } from './client';
import type { ProductSummaryDTO, ProductDetailsDTO, ProductCreateDTO, ProductUpdateDTO } from '../models';

const BASE = '/products';

export const productApi = {
  findAll: () => api.get<ProductSummaryDTO[]>(BASE).then((r) => r.data),

  findById: (id: number) =>
    api.get<ProductDetailsDTO>(`${BASE}/${id}`).then((r) => r.data),

  create: (dto: ProductCreateDTO) =>
    api.post<ProductDetailsDTO>(BASE, dto).then((r) => r.data),

  update: (id: number, dto: ProductUpdateDTO) =>
    api.put<ProductDetailsDTO>(`${BASE}/${id}`, dto).then((r) => r.data),

  updateStatus: (id: number, status: { status: import('../models').ProductStatus }) =>
    api.patch<ProductDetailsDTO>(`${BASE}/${id}/status`, status).then((r) => r.data),

  remove: (id: number) => api.delete(`${BASE}/${id}`),
};
