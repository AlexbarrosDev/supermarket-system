import { api } from './client';
import type {
  SaleSummaryDTO,
  SaleDetailsDTO,
  SaleCreateDTO,
  SaleStatusUpdateDTO,
} from '../models';

const BASE = '/sales';

export const saleApi = {
  findAll: () => api.get<SaleSummaryDTO[]>(BASE).then((r) => r.data),

  findById: (id: number) =>
    api.get<SaleDetailsDTO>(`${BASE}/${id}`).then((r) => r.data),

  create: (dto: SaleCreateDTO) =>
    api.post<SaleDetailsDTO>(BASE, dto).then((r) => r.data),

  updateStatus: (id: number, dto: SaleStatusUpdateDTO) =>
    api.patch<SaleDetailsDTO>(`${BASE}/${id}/status`, dto).then((r) => r.data),

  remove: (id: number) => api.delete(`${BASE}/${id}`),
};
