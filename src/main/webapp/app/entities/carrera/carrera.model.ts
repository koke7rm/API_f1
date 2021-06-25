import { IPiloto } from 'app/entities/piloto/piloto.model';

export interface ICarrera {
  id?: number;
  nombreCircuito?: string | null;
  pais?: string | null;
  ganador?: IPiloto | null;
  segundoPuesto?: IPiloto | null;
  tercerPuesto?: IPiloto | null;
  noTerminans?: IPiloto[] | null;
}

export class Carrera implements ICarrera {
  constructor(
    public id?: number,
    public nombreCircuito?: string | null,
    public pais?: string | null,
    public ganador?: IPiloto | null,
    public segundoPuesto?: IPiloto | null,
    public tercerPuesto?: IPiloto | null,
    public noTerminans?: IPiloto[] | null
  ) {}
}

export function getCarreraIdentifier(carrera: ICarrera): number | undefined {
  return carrera.id;
}
