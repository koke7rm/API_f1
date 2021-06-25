import { ICarrera } from 'app/entities/carrera/carrera.model';

export interface IPiloto {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  edad?: number | null;
  coche?: string | null;
  carrerasGanadas?: ICarrera[] | null;
  segundosPuestos?: ICarrera[] | null;
  tercerPuestos?: ICarrera[] | null;
  carrerasInacabadas?: ICarrera[] | null;
}

export class Piloto implements IPiloto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public edad?: number | null,
    public coche?: string | null,
    public carrerasGanadas?: ICarrera[] | null,
    public segundosPuestos?: ICarrera[] | null,
    public tercerPuestos?: ICarrera[] | null,
    public carrerasInacabadas?: ICarrera[] | null
  ) {}
}

export function getPilotoIdentifier(piloto: IPiloto): number | undefined {
  return piloto.id;
}
