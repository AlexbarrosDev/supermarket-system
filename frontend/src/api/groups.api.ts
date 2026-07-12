import { api } from './client';
import type { GroupDetailsDTO, GroupCreateDTO, GroupUpdateDTO } from '../models';

const BASE = '/groups';

export const groupApi = {
  findAll: () => api.get<GroupDetailsDTO[]>(BASE).then((r) => r.data),

  findById: (id: number) =>
    api.get<GroupDetailsDTO>(`${BASE}/${id}`).then((r) => r.data),

  create: (dto: GroupCreateDTO) =>
    api.post<GroupDetailsDTO>(BASE, dto).then((r) => r.data),

  update: (id: number, dto: GroupUpdateDTO) =>
    api.put<GroupDetailsDTO>(`${BASE}/${id}`, dto).then((r) => r.data),

  remove: (id: number) => api.delete(`${BASE}/${id}`),
};
