import { api } from './client';
import type { ClientSummaryDTO, ClientDetailsDTO, ClientCreateDTO, ClientUpdateDTO, ClientStatusUpdateDTO } from '../models';

const BASE = '/clients';

export const clientApi = {
  findAll: () => api.get<ClientSummaryDTO[]>(BASE).then((r) => r.data),

  findById: (id: number) =>
    api.get<ClientDetailsDTO>(`${BASE}/${id}`).then((r) => r.data),

  create: (dto: ClientCreateDTO) =>
    api.post<ClientDetailsDTO>(BASE, dto).then((r) => r.data),

  update: (id: number, dto: ClientUpdateDTO) =>
    api.put<ClientDetailsDTO>(`${BASE}/${id}`, dto).then((r) => r.data),

  updateStatus: (id: number, dto: ClientStatusUpdateDTO) =>
    api.patch<ClientDetailsDTO>(`${BASE}/${id}/status`, dto).then((r) => r.data),
};
