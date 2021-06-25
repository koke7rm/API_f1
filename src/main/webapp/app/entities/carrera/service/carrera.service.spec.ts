import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICarrera, Carrera } from '../carrera.model';

import { CarreraService } from './carrera.service';

describe('Service Tests', () => {
  describe('Carrera Service', () => {
    let service: CarreraService;
    let httpMock: HttpTestingController;
    let elemDefault: ICarrera;
    let expectedResult: ICarrera | ICarrera[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CarreraService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombreCircuito: 'AAAAAAA',
        pais: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Carrera', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Carrera()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Carrera', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombreCircuito: 'BBBBBB',
            pais: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Carrera', () => {
        const patchObject = Object.assign(
          {
            nombreCircuito: 'BBBBBB',
          },
          new Carrera()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Carrera', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombreCircuito: 'BBBBBB',
            pais: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Carrera', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCarreraToCollectionIfMissing', () => {
        it('should add a Carrera to an empty array', () => {
          const carrera: ICarrera = { id: 123 };
          expectedResult = service.addCarreraToCollectionIfMissing([], carrera);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(carrera);
        });

        it('should not add a Carrera to an array that contains it', () => {
          const carrera: ICarrera = { id: 123 };
          const carreraCollection: ICarrera[] = [
            {
              ...carrera,
            },
            { id: 456 },
          ];
          expectedResult = service.addCarreraToCollectionIfMissing(carreraCollection, carrera);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Carrera to an array that doesn't contain it", () => {
          const carrera: ICarrera = { id: 123 };
          const carreraCollection: ICarrera[] = [{ id: 456 }];
          expectedResult = service.addCarreraToCollectionIfMissing(carreraCollection, carrera);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(carrera);
        });

        it('should add only unique Carrera to an array', () => {
          const carreraArray: ICarrera[] = [{ id: 123 }, { id: 456 }, { id: 90542 }];
          const carreraCollection: ICarrera[] = [{ id: 123 }];
          expectedResult = service.addCarreraToCollectionIfMissing(carreraCollection, ...carreraArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const carrera: ICarrera = { id: 123 };
          const carrera2: ICarrera = { id: 456 };
          expectedResult = service.addCarreraToCollectionIfMissing([], carrera, carrera2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(carrera);
          expect(expectedResult).toContain(carrera2);
        });

        it('should accept null and undefined values', () => {
          const carrera: ICarrera = { id: 123 };
          expectedResult = service.addCarreraToCollectionIfMissing([], null, carrera, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(carrera);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
