import dayjs from 'dayjs';

export const formatCurrency = (value: number): string =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);

export const formatDate = (iso: string): string =>
  dayjs(iso).format('DD/MM/YYYY HH:mm');

export const formatDateShort = (iso: string): string =>
  dayjs(iso).format('DD/MM/YYYY');
